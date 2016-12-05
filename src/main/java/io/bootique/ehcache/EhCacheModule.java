package io.bootique.ehcache;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;
import io.bootique.shutdown.ShutdownManager;

import javax.cache.CacheManager;

public class EhCacheModule extends ConfigModule {

    @Singleton
    @Provides
    CacheManager provideCacheManager(ConfigurationFactory configurationFactory, ShutdownManager shutdownManager) {
        return configurationFactory.config(EhCacheFactory.class, configPrefix).createManager(shutdownManager);
    }
}
