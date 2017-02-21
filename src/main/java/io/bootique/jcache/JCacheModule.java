package io.bootique.jcache;

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.MapBinder;
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

    /**
     * @param binder DI binder
     * @return a binder for configs.
     * @deprecated since 0.2 use {@link #extend(Binder)} to get an extender object, and
     * then call {@link JCacheModuleExtender#setConfiguration(String, Configuration)} or similar.
     */
    @Deprecated
    public static MapBinder<String, Configuration<?, ?>> contributeConfiguration(Binder binder) {
        return extend(binder).contributeConfiguration();
    }

    @Override
    public void configure(Binder binder) {
        JCacheModule.extend(binder).initAllExtensions();
    }

    @Singleton
    @Provides
    CacheManager provideCacheManager(ConfigurationFactory configurationFactory,
                                     ShutdownManager shutdownManager,
                                     Map<String, Configuration<?, ?>> configs) {
        return configurationFactory.config(JCacheFactory.class, configPrefix).createManager(configs, shutdownManager);
    }
}
