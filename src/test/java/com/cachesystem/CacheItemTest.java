package com.cachesystem;
import com.cachesystem.cache.CacheItem;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CacheItemTest {

    @Test
    void testCacheItemCreation() {
        CacheItem<String, Integer> item = new CacheItem<>("key1", 100);

        assertEquals("key1", item.getKey());
        assertEquals(100, item.getValue());
        assertTrue(item.getTimestamp() > 0); // Timestamp should be set
    }

    @Test
    void testSetKey() {
        CacheItem<String, Integer> item = new CacheItem<>("key1", 100);
        item.setKey("key2");
        assertEquals("key2", item.getKey());
    }

    @Test
    void testSetValue() {
        CacheItem<String, Integer> item = new CacheItem<>("key1", 100);
        item.setValue(200);
        assertEquals(200, item.getValue());
    }

    @Test
    void testSetTimestamp() {
        CacheItem<String, Integer> item = new CacheItem<>("key1", 100);
        long newTimestamp = System.currentTimeMillis();
        item.setTimestamp(newTimestamp);
        assertEquals(newTimestamp, item.getTimestamp());
    }
}