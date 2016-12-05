package io.bootique.ehcache;

import io.bootique.test.BQTestRuntime;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.MutableEntry;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EhCacheModuleIT {

    @ClassRule
    public static BQTestFactory TEST_FACTORY = new BQTestFactory();

    private static CacheManager CM;

    @BeforeClass
    public static void initCaches() {

        BQTestRuntime runtime = TEST_FACTORY.app("-c", "classpath:ehcache1.yml")
                .autoLoadModules()
                .createRuntime();

        CM = runtime.getRuntime().getInstance(CacheManager.class);
    }

    @Test
    public void testCache() {

        Cache<Integer, String> cache = CM.getCache("2entry", Integer.class, String.class);
        Assert.assertNotNull(cache);

        cache.put(3, "three");
        assertEquals("three", cache.get(3));

        cache.put(4, "four");
        assertEquals("four", cache.get(4));

        assertNull(cache.get(5));
    }

    @Test
    public void testCacheExpiry() throws InterruptedException {

        Cache<String, Integer> cache = CM.getCache("expiring", String.class, Integer.class);
        Assert.assertNotNull(cache);

        cache.put("five", 5);

        assertEquals(Integer.valueOf(5), cache.get("five"));
        Thread.sleep(101);

        assertNull(cache.get("five"));
    }

    @Test
    public void testEntryFactory() {

        Cache<String, Integer> cache = CM.getCache("entryfactory", String.class, Integer.class);
        Assert.assertNotNull(cache);

        assertNull(cache.get("one"));
        AtomicInteger counter = new AtomicInteger();

        EntryProcessor<String, Integer, Integer> mockEntryMaker = Mockito.mock(EntryProcessor.class);
        when(mockEntryMaker.process(any(MutableEntry.class))).thenAnswer(new Answer<Integer>() {

            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {

                MutableEntry<String, Integer> e = (MutableEntry<String, Integer>) invocation.getArguments()[0];
                if (!e.exists()) {
                    e.setValue(counter.incrementAndGet());
                }

                return e.getValue();
            }
        });

        assertEquals(Integer.valueOf(1), cache.invoke("one", mockEntryMaker));
        assertEquals(Integer.valueOf(1), cache.invoke("one", mockEntryMaker));
        assertEquals(Integer.valueOf(2), cache.invoke("two", mockEntryMaker));
        assertEquals(Integer.valueOf(2), cache.invoke("two", mockEntryMaker));

        verify(mockEntryMaker, times(4));
    }


}
