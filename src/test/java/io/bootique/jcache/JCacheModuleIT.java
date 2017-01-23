package io.bootique.jcache;

import io.bootique.test.BQTestRuntime;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.Factory;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ExpiryPolicy;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JCacheModuleIT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testNoConfig() throws InterruptedException {

        BQTestRuntime runtime = testFactory.app()
                .autoLoadModules()
                .createRuntime();

        CacheManager cm = runtime.getRuntime().getInstance(CacheManager.class);

        assertNotNull(cm);

        Set<String> names = new HashSet<>();
        cm.getCacheNames().forEach(names::add);
        assertTrue(names.isEmpty());
    }

    @Test
    public void testContributedConfig() throws InterruptedException {

        Factory<ExpiryPolicy> _100ms = CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.MILLISECONDS, 100));
        Configuration<Long, Long> boundConfig = new MutableConfiguration<Long, Long>()
                .setTypes(Long.class, Long.class)
                .setExpiryPolicyFactory(_100ms);

        BQTestRuntime runtime = testFactory.app()
                .autoLoadModules()
                .module(b -> JCacheModule.contributeConfiguration(b).addBinding("fromconfig").toInstance(boundConfig))
                .createRuntime();

        CacheManager cm = runtime.getRuntime().getInstance(CacheManager.class);

        // test loaded caches

        Set<String> names = new HashSet<>();
        cm.getCacheNames().forEach(names::add);

        assertEquals(new HashSet<String>(asList("fromconfig")), names);

        // test cache config
        Cache<Long, Long> cache = cm.getCache("fromconfig", Long.class, Long.class);
        assertNotNull(cache);

        cache.put(5l, 10l);

        assertEquals(Long.valueOf(10), cache.get(5l));
        Thread.sleep(101);

        assertNull(cache.get(5l));
    }

    @Test
    public void testContributedAndXmlConfig() throws InterruptedException {

        Factory<ExpiryPolicy> _100ms = CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.MILLISECONDS, 100));
        Configuration<Long, Long> boundConfig = new MutableConfiguration<Long, Long>()
                .setTypes(Long.class, Long.class)
                .setExpiryPolicyFactory(_100ms);

        BQTestRuntime runtime = testFactory.app("-c", Objects.requireNonNull("classpath:ehcache2.yml"))
                .autoLoadModules()
                .module(b -> JCacheModule.contributeConfiguration(b).addBinding("fromconfig").toInstance(boundConfig))
                .createRuntime();

        CacheManager cm = runtime.getRuntime().getInstance(CacheManager.class);

        // test loaded caches

        Set<String> names = new HashSet<>();
        cm.getCacheNames().forEach(names::add);

        assertEquals(new HashSet<String>(asList("fromxml", "fromconfig")), names);

        // test cache config
        Cache<Long, Long> cache = cm.getCache("fromconfig", Long.class, Long.class);
        assertNotNull(cache);

        cache.put(5l, 10l);

        assertEquals(Long.valueOf(10), cache.get(5l));
        Thread.sleep(101);

        assertNull(cache.get(5l));
    }
}
