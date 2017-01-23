package io.bootique.jcache;

import com.google.inject.Module;
import io.bootique.BQModule;
import io.bootique.BQModuleProvider;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class JCacheModuleProvider implements BQModuleProvider {

    @Override
    public Module module() {
        return new JCacheModule();
    }

    @Override
    public Map<String, Type> configs() {
        // TODO: config prefix is hardcoded. Refactor away from ConfigModule, and make provider
        // generate config prefix, reusing it in metadata...
        return Collections.singletonMap("jcache", JCacheFactory.class);
    }

    @Override
    public BQModule.Builder moduleBuilder() {
        return BQModuleProvider.super
                .moduleBuilder()
                .description("Provides configuration for JCache subsystem. Module itself does NOT include a JCache provider."
                        + " The users will need to add a provider of their choice to the application classpath.");
    }
}
