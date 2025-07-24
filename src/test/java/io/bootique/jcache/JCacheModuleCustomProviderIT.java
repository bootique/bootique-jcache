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

import javax.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertTrue;

@BQTest
public class JCacheModuleCustomProviderIT {

    @BQApp(skipRun = true)
    static BQRuntime app = Bootique.app("-c", "classpath:custom-provider.yml")
            .autoLoadModules()
            .createRuntime();

    @Test
    public void provider() {
        CacheManager cm = app.getInstance(CacheManager.class);
        assertTrue(cm instanceof CustomProvider.CustomCacheManager);
    }
}
