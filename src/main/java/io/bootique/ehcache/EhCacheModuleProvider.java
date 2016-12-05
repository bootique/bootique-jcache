package io.bootique.ehcache;

import com.google.inject.Module;
import io.bootique.BQModuleProvider;

public class EhCacheModuleProvider implements BQModuleProvider {

    @Override
    public Module module() {
        return new EhCacheModule();
    }
}
