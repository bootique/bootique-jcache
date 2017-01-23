package io.bootique.jcache;

import com.google.inject.Module;
import io.bootique.BQModuleProvider;

public class JCacheModuleProvider implements BQModuleProvider {

    @Override
    public Module module() {
        return new JCacheModule();
    }
}
