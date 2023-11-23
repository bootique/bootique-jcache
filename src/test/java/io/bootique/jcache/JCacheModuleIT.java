/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.bootique.jcache;

import io.bootique.BQRuntime;
import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestFactory;
import io.bootique.junit5.BQTestTool;
import org.junit.jupiter.api.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.Factory;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@BQTest
public class JCacheModuleIT {

    @BQTestTool
    final BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void noConfig() {

        BQRuntime runtime = testFactory.app()
                .autoLoadModules()
                .createRuntime();

        CacheManager cm = runtime.getInstance(CacheManager.class);

        assertNotNull(cm);

        Set<String> names = new HashSet<>();
        cm.getCacheNames().forEach(names::add);
        assertTrue(names.isEmpty());
    }

    @Test
    public void contributedConfig() throws InterruptedException {

        Factory<ExpiryPolicy> _100ms = CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.MILLISECONDS, 100));
        Configuration<Long, Long> boundConfig = new MutableConfiguration<Long, Long>()
                .setTypes(Long.class, Long.class)
                .setExpiryPolicyFactory(_100ms);

        BQRuntime runtime = testFactory.app()
                .autoLoadModules()
                .module(b -> JCacheModule.extend(b).setConfiguration("fromconfig", boundConfig))
                .createRuntime();

        CacheManager cm = runtime.getInstance(CacheManager.class);

        // test loaded caches

        Set<String> names = new HashSet<>();
        cm.getCacheNames().forEach(names::add);

        assertEquals(Collections.singleton("fromconfig"), names);

        // test cache config
        Cache<Long, Long> cache = cm.getCache("fromconfig", Long.class, Long.class);
        assertNotNull(cache);

        cache.put(5L, 10L);

        assertEquals(Long.valueOf(10), cache.get(5L));
        Thread.sleep(101);

        assertNull(cache.get(5L));
    }

    @Test
    public void contributedAndXmlConfig() throws InterruptedException {

        Factory<ExpiryPolicy> _100ms = CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.MILLISECONDS, 100));
        Configuration<Long, Long> boundConfig = new MutableConfiguration<Long, Long>()
                .setTypes(Long.class, Long.class)
                .setExpiryPolicyFactory(_100ms);

        BQRuntime runtime = testFactory.app("-c", "classpath:ehcache2.yml")
                .autoLoadModules()
                .module(b -> JCacheModule.extend(b).setConfiguration("fromconfig", boundConfig))
                .createRuntime();

        CacheManager cm = runtime.getInstance(CacheManager.class);

        // test loaded caches

        Set<String> names = new HashSet<>();
        cm.getCacheNames().forEach(names::add);

        assertEquals(new HashSet<>(asList("fromxml", "fromconfig")), names);

        // test cache config
        Cache<Long, Long> cache = cm.getCache("fromconfig", Long.class, Long.class);
        assertNotNull(cache);

        cache.put(5L, 10L);

        assertEquals(Long.valueOf(10), cache.get(5L));
        Thread.sleep(101);

        assertNull(cache.get(5L));
    }
}
