package io.bootique.jcache;

import io.bootique.test.junit.BQModuleProviderChecker;
import org.junit.Test;

public class JCacheModuleProviderTest {

    @Test
    public void testAutoLoading() {
        BQModuleProviderChecker.testAutoLoadable(JCacheModuleProvider.class);
    }
}
