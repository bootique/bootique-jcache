package io.bootique.ehcache;

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;
import io.bootique.shutdown.ShutdownManager;

import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.util.Map;

public class EhCacheModule extends ConfigModule {

    public static MapBinder<String, Configuration<?, ?>> contributeConfiguration(Binder binder) {
        TypeLiteral<String> keyLiteral = new TypeLiteral<String>() {};
        TypeLiteral<Configuration<?, ?>> valueLiteral = new TypeLiteral<Configuration<?, ?>>() {};
        return MapBinder.newMapBinder(binder, keyLiteral, valueLiteral);
    }

    @Override
    public void configure(Binder binder) {
        EhCacheModule.contributeConfiguration(binder);
    }

    @Singleton
    @Provides
    CacheManager provideCacheManager(ConfigurationFactory configurationFactory,
                                     ShutdownManager shutdownManager,
                                     Map<String, Configuration<?, ?>> configs) {
        return configurationFactory.config(EhCacheFactory.class, configPrefix).createManager(configs, shutdownManager);
    }
}
