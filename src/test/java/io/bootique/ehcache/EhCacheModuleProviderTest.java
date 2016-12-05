package io.bootique.ehcache;

import io.bootique.test.junit.BQModuleProviderChecker;
import org.junit.Test;

public class EhCacheModuleProviderTest {

    @Test
    public void testAutoLoading() {
        BQModuleProviderChecker.testPresentInJar(EhCacheModuleProvider.class);
    }
}
