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

import javax.cache.configuration.Configuration;

import io.bootique.di.Binder;
import io.bootique.di.MapBuilder;
import io.bootique.di.TypeLiteral;

/**
 * @since 0.2
 */
public class JCacheModuleExtender {

    private Binder binder;

    private MapBuilder<String, Configuration<?, ?>> configurations;

    JCacheModuleExtender(Binder binder) {
        this.binder = binder;
    }

    JCacheModuleExtender initAllExtensions() {
        contributeConfiguration();
        return this;
    }

    public JCacheModuleExtender setConfiguration(String name, Configuration<?, ?> config) {
        contributeConfiguration().put(name, config);
        return this;
    }

    public JCacheModuleExtender setConfiguration(String name, Class<? extends Configuration<?, ?>> configType) {
        contributeConfiguration().put(name, configType);
        return this;
    }

    protected MapBuilder<String, Configuration<?, ?>> contributeConfiguration() {

        if (configurations == null) {
            TypeLiteral<String> keyLiteral = new TypeLiteral<String>() {
            };
            TypeLiteral<Configuration<?, ?>> valueLiteral = new TypeLiteral<Configuration<?, ?>>() {
            };
            configurations = binder.bindMap(keyLiteral, valueLiteral);
        }

        return configurations;
    }
}
