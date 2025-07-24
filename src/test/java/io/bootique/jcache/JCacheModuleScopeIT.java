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
import io.bootique.command.CommandOutcome;
import org.junit.jupiter.api.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

public class JCacheModuleScopeIT {

    @Test
    public void scope() throws ExecutionException, InterruptedException {

        // probabilistically reproduce https://github.com/bootique/bootique-jcache/issues/15
        try (ExecutorService pool = Executors.newFixedThreadPool(2)) {

            List<Future<CommandOutcome>> futures = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                futures.add(pool.submit(() -> {
                    try {
                        BQRuntime rt = Bootique.app("-c", "classpath:ehcache-scope.yml").autoLoadModules().createRuntime();
                        Cache<String, String> c1 = rt.getInstance(CacheManager.class).getCache("scope-test-cache");
                        c1.put("a", "A");

                        Thread.sleep(50);
                        String fromCache = c1.get("a");
                        assertEquals("A", fromCache);
                        rt.shutdown();
                        return CommandOutcome.succeeded();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return CommandOutcome.failed(-1, e);
                    }
                }));
            }

            for (Future<CommandOutcome> f : futures) {
                CommandOutcome o = f.get();
                assertTrue(o.isSuccess(), () -> o.toString());
            }
        }
    }
}
