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

import io.bootique.BQModule;
import io.bootique.ModuleCrate;
import io.bootique.config.ConfigurationFactory;
import io.bootique.di.Binder;
import io.bootique.di.Provides;

import javax.cache.CacheManager;
import jakarta.inject.Singleton;

public class JCacheModule implements BQModule {

    private static final String CONFIG_PREFIX = "jcache";

    /**
     * Returns an instance of {@link JCacheModuleExtender} used by downstream modules to load custom extensions to the
     * JCacheModule. Should be invoked from a downstream Module's "configure" method.
     *
     * @param binder DI binder passed to the Module that invokes this method.
     * @return an instance of {@link JCacheModuleExtender} that can be used to load custom extensions to the JCacheModule.
     */
    public static JCacheModuleExtender extend(Binder binder) {
        return new JCacheModuleExtender(binder);
    }

    @Override
    public ModuleCrate crate() {
        return ModuleCrate.of(this)
                .description("Integrates configuration for the JCache subsystem. Module itself does not include a JCache " +
                        "provider. Users will need to add a provider of their choice to the application classpath.")
                .config(CONFIG_PREFIX, JCacheFactory.class)
                .build();
    }

    @Override
    public void configure(Binder binder) {
        JCacheModule.extend(binder).initAllExtensions();
    }

    @Singleton
    @Provides
    CacheManager provideCacheManager(ConfigurationFactory configFactory) {
        return configFactory.config(JCacheFactory.class, CONFIG_PREFIX).create();
    }
}
