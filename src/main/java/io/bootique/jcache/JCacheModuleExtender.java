package io.bootique.jcache;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;

import javax.cache.configuration.Configuration;

/**
 * @since 0.2
 */
public class JCacheModuleExtender {

    private Binder binder;

    private MapBinder<String, Configuration<?, ?>> configurations;

    JCacheModuleExtender(Binder binder) {
        this.binder = binder;
    }

    JCacheModuleExtender initAllExtensions() {
        contributeConfiguration();
        return this;
    }

    public JCacheModuleExtender setConfiguration(String name, Configuration<?, ?> config) {
        contributeConfiguration().addBinding(name).toInstance(config);
        return this;
    }

    public JCacheModuleExtender setConfiguration(String name, Class<? extends Configuration<?, ?>> configType) {
        contributeConfiguration().addBinding(name).to(configType);
        return this;
    }

    protected MapBinder<String, Configuration<?, ?>> contributeConfiguration() {

        if (configurations == null) {
            TypeLiteral<String> keyLiteral = new TypeLiteral<String>() {
            };
            TypeLiteral<Configuration<?, ?>> valueLiteral = new TypeLiteral<Configuration<?, ?>>() {
            };
            configurations = MapBinder.newMapBinder(binder, keyLiteral, valueLiteral);
        }

        return configurations;
    }
}
