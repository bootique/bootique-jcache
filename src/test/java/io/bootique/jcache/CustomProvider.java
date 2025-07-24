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

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Properties;

public class CustomProvider implements CachingProvider {

    @Override
    public void close() {
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        return new CustomCacheManager();
    }

    @Override
    public ClassLoader getDefaultClassLoader() {
        return getClass().getClassLoader();
    }

    @Override
    public URI getDefaultURI() {
        return null;
    }

    @Override
    public Properties getDefaultProperties() {
        return new Properties();
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        return new CustomCacheManager();
    }

    @Override
    public CacheManager getCacheManager() {
        return new CustomCacheManager();
    }

    @Override
    public void close(ClassLoader classLoader) {
    }

    @Override
    public void close(URI uri, ClassLoader classLoader) {
    }

    @Override
    public boolean isSupported(OptionalFeature optionalFeature) {
        return false;
    }

    static class CustomCacheManager implements CacheManager {
        @Override
        public void close() {

        }

        @Override
        public CachingProvider getCachingProvider() {
            return null;
        }

        @Override
        public URI getURI() {
            return null;
        }

        @Override
        public ClassLoader getClassLoader() {
            return null;
        }

        @Override
        public Properties getProperties() {
            return null;
        }

        @Override
        public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(String cacheName, C configuration) throws IllegalArgumentException {
            return null;
        }

        @Override
        public <K, V> Cache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
            return null;
        }

        @Override
        public <K, V> Cache<K, V> getCache(String cacheName) {
            return null;
        }

        @Override
        public Iterable<String> getCacheNames() {
            return null;
        }

        @Override
        public void destroyCache(String cacheName) {

        }

        @Override
        public void enableManagement(String cacheName, boolean enabled) {

        }

        @Override
        public void enableStatistics(String cacheName, boolean enabled) {

        }

        @Override
        public boolean isClosed() {
            return false;
        }

        @Override
        public <T> T unwrap(Class<T> clazz) {
            return null;
        }
    }
}
