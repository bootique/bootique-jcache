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
import io.bootique.Bootique;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.MutableEntry;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@BQTest
public class JCacheModule_XMLConfigIT {

    @BQApp(skipRun = true)
    static final BQRuntime app = Bootique.app("-c", "classpath:ehcache1.yml")
            .autoLoadModules()
            .createRuntime();

    @Test
    public void cache() {

        Cache<Integer, String> cache = app.getInstance(CacheManager.class)
                .getCache("2entry", Integer.class, String.class);

        assertNotNull(cache);

        cache.put(3, "three");
        assertEquals("three", cache.get(3));

        cache.put(4, "four");
        assertEquals("four", cache.get(4));

        assertNull(cache.get(5));
    }

    @Test
    public void cacheExpiry() throws InterruptedException {

        Cache<String, Integer> cache = app.getInstance(CacheManager.class)
                .getCache("expiring", String.class, Integer.class);
        assertNotNull(cache);

        cache.put("five", 5);

        assertEquals(Integer.valueOf(5), cache.get("five"));
        Thread.sleep(101);

        assertNull(cache.get("five"));
    }

    @Test
    public void entryFactory() {

        Cache<String, Integer> cache = app.getInstance(CacheManager.class)
                .getCache("entryfactory", String.class, Integer.class);
        assertNotNull(cache);

        assertNull(cache.get("one"));
        AtomicInteger counter = new AtomicInteger();

        EntryProcessor<String, Integer, Integer> mockEntryMaker = Mockito.mock(EntryProcessor.class);
        when(mockEntryMaker.process(any(MutableEntry.class))).thenAnswer((Answer<Integer>) invocation -> {

            MutableEntry<String, Integer> e = (MutableEntry<String, Integer>) invocation.getArguments()[0];
            if (!e.exists()) {
                e.setValue(counter.incrementAndGet());
            }

            return e.getValue();
        });

        assertEquals(Integer.valueOf(1), cache.invoke("one", mockEntryMaker));
        assertEquals(Integer.valueOf(1), cache.invoke("one", mockEntryMaker));
        assertEquals(Integer.valueOf(2), cache.invoke("two", mockEntryMaker));
        assertEquals(Integer.valueOf(2), cache.invoke("two", mockEntryMaker));

        verify(mockEntryMaker, times(4));
    }


}
