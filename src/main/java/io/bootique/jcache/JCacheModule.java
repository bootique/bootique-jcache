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

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;
import io.bootique.shutdown.ShutdownManager;

import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.util.Map;

public class JCacheModule extends ConfigModule {

    /**
     * Returns an instance of {@link JCacheModuleExtender} used by downstream modules to load custom extensions to the
     * JCacheModule. Should be invoked from a downstream Module's "configure" method.
     *
     * @param binder DI binder passed to the Module that invokes this method.
     * @return an instance of {@link JCacheModuleExtender} that can be used to load custom extensions to the JCacheModule.
     * @since 0.2
     */
    public static JCacheModuleExtender extend(Binder binder) {
        return new JCacheModuleExtender(binder);
    }

    @Override
    public void configure(Binder binder) {
        JCacheModule.extend(binder).initAllExtensions();
    }

    @Singleton
    @Provides
    CacheManager provideCacheManager(ConfigurationFactory configFactory,
                                     ShutdownManager shutdownManager,
                                     Map<String, Configuration<?, ?>> configs) {

        return config(JCacheFactory.class, configFactory).createManager(configs, shutdownManager);
    }
}
