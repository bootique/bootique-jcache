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

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.resource.ResourceFactory;
import io.bootique.shutdown.ShutdownManager;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;

@BQConfig("Configures JCache")
public class JCacheFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(JCacheFactory.class);

    private final ShutdownManager shutdownManager;
    private final Map<String, Configuration<?, ?>> cacheConfigurations;

    private Class<?> provider;
    private List<ResourceFactory> configs;

    @Inject
    public JCacheFactory(ShutdownManager shutdownManager, Map<String, Configuration<?, ?>> cacheConfigurations) {
        this.shutdownManager = shutdownManager;
        this.cacheConfigurations = cacheConfigurations;
    }

    @BQConfigProperty("""
            A list of resource URLs pointing to cache configuration files in the format understood by the underlying \
            JCache provider.""")
    public void setConfigs(List<ResourceFactory> configs) {
        this.configs = Objects.requireNonNull(configs);
    }

    /**
     * @since 4.0
     */
    @BQConfigProperty("""
            A specific 'javax.cache.spi.CachingProvider' class that will handle caching. If not specified, the first \
            available CachingProvider discovered via ServiceLoader is used""")
    public void setProvider(Class<?> provider) {
        this.provider = provider;
    }

    public CacheManager create() {

        CachingProvider provider = findProvider();
        shutdownManager.onShutdown(provider);

        CacheManager manager = getConfigUri().map(u -> provider.getCacheManager(u, null)).orElse(provider.getCacheManager());
        shutdownManager.onShutdown(manager);

        // now load contributed configs
        cacheConfigurations.forEach(manager::createCache);

        return manager;
    }

    private Optional<URI> getConfigUri() {

        if (configs == null) {
            return Optional.empty();
        }

        return switch (configs.size()) {
            case 0 -> Optional.empty();
            case 1 -> {
                try {
                    yield Optional.of(configs.getFirst().getUrl().toURI());
                } catch (URISyntaxException e) {
                    throw new IllegalStateException("Error converting config to URI: " + configs.getFirst());
                }
            }
            // TODO: how do we merge multiple configs?
            default ->
                    throw new IllegalStateException("More than one JCache configuration specified. Currently unsupported: " + configs);
        };
    }

    private CachingProvider findProvider() {
        return provider != null ? loadForType(provider) : loadFromServiceLoader();
    }

    private CachingProvider loadForType(Class<?> providerType) {

        if (!CachingProvider.class.isAssignableFrom(providerType)) {
            throw new RuntimeException("Type '" + providerType + "' is not a javax.cache.spi.CachingProvider");
        }

        try {
            return (CachingProvider) providerType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("CachingProvider type '" + providerType + "' is invalid or failed to load", e);
        }
    }

    private CachingProvider loadFromServiceLoader() {
        List<ServiceLoader.Provider<CachingProvider>> providers = ServiceLoader.load(CachingProvider.class).stream().toList();

        if (providers.isEmpty()) {
            throw new RuntimeException("No CachingProviders have been configured");
        }

        CachingProvider provider = providers.getFirst().get();

        if (providers.size() > 1) {
            LOGGER.warn("Found multiple CachingProviders. Using the first one: {}", provider.getClass().getName());
        }

        return provider;
    }
}
