package com.cachesystem;

import com.cachesystem.cache.Cache;
import com.cachesystem.cache.CacheFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LRUCacheTest {
    // LRU Eviction Strategy Test
    @Test
    void testCacheEvictionLRU() {
        Cache<String, String> cache = CacheFactory.createCache(2, "LRU", 0);

        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        // "key1" should be evicted as it is the least recently used
        assertNull(cache.get("key1"));
        assertEquals("value2", cache.get("key2"));
        assertEquals("value3", cache.get("key3"));
    }

}
