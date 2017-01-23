package io.bootique.jcache;

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

public class JCacheModule extends ConfigModule {

    public static MapBinder<String, Configuration<?, ?>> contributeConfiguration(Binder binder) {
        TypeLiteral<String> keyLiteral = new TypeLiteral<String>() {};
        TypeLiteral<Configuration<?, ?>> valueLiteral = new TypeLiteral<Configuration<?, ?>>() {};
        return MapBinder.newMapBinder(binder, keyLiteral, valueLiteral);
    }

    @Override
    public void configure(Binder binder) {
        JCacheModule.contributeConfiguration(binder);
    }

    @Singleton
    @Provides
    CacheManager provideCacheManager(ConfigurationFactory configurationFactory,
                                     ShutdownManager shutdownManager,
                                     Map<String, Configuration<?, ?>> configs) {
        return configurationFactory.config(JCacheFactory.class, configPrefix).createManager(configs, shutdownManager);
    }
}
