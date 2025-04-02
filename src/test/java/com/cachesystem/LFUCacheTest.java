package com.cachesystem;

import com.cachesystem.cache.Cache;
import com.cachesystem.cache.CacheFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LFUCacheTest {
    // LFU Eviction Strategy Test
    @Test
    void testCacheEvictionLFU() {
        Cache<String, String> cache = CacheFactory.createCache(2, "LFU", 0);

        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.get("key1"); // Access "key1" once
        cache.put("key3", "value3");

        // "key2" should be evicted as it is the least frequently used
        assertNull(cache.get("key2"));
        assertEquals("value1", cache.get("key1"));
        assertEquals("value3", cache.get("key3"));
    }

}
