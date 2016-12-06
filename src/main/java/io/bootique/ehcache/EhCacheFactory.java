package io.bootique.ehcache;

import io.bootique.resource.ResourceFactory;
import io.bootique.shutdown.ShutdownManager;
import org.ehcache.jsr107.EhcacheCachingProvider;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class EhCacheFactory {

    private List<ResourceFactory> configs;

    public EhCacheFactory() {
        this.configs = Collections.emptyList();
    }

    public void setConfigs(List<ResourceFactory> configs) {
        this.configs = Objects.requireNonNull(configs);
    }

    public CacheManager createManager(Map<String, Configuration<?, ?>> configs, ShutdownManager shutdownManager) {
        CachingProvider provider = Caching.getCachingProvider(EhcacheCachingProvider.class.getName());
        shutdownManager.addShutdownHook(provider);

        CacheManager manager = getConfigUri().map(u -> provider.getCacheManager(u, null)).orElse(provider.getCacheManager());
        shutdownManager.addShutdownHook(manager);

        // now load contributed configs
        configs.forEach(manager::createCache);

        return manager;
    }

    private Optional<URI> getConfigUri() {

        switch (configs.size()) {
            case 0:
                return Optional.empty();
            case 1:
                try {
                    return Optional.of(configs.get(0).getUrl().toURI());
                } catch (URISyntaxException e) {
                    throw new IllegalStateException("Error confverting config to URI: " + configs.get(0));
                }
            default:
                // TODO: how do we merge multiple configs?
                throw new IllegalStateException("More than one EhCache configuration specified. Currently unsupported");
        }
    }
}
