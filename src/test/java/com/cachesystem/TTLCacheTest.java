package com.cachesystem;

import com.cachesystem.eviction.TTLCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

class TTLCacheTest {
    private TTLCache<String, String> ttlCache;

    @BeforeEach
    void setUp() {
        ttlCache = new TTLCache<>(2000); // TTL = 2 seconds
    }

    @Test
    void testOnAccessStoresTimestamp() {
        ttlCache.onAccess("key1");
        assertFalse(ttlCache.isExpired("key1")); // Key should not be expired immediately
    }

    @Test
    void testIsExpiredReturnsTrueAfterTTL() throws InterruptedException {
        ttlCache.onAccess("key1");

        // Wait for TTL to expire
        TimeUnit.MILLISECONDS.sleep(2500);

        assertTrue(ttlCache.isExpired("key1")); // Key should be expired
    }

    @Test
    void testClearExpiredCacheRemovesExpiredItems() throws InterruptedException {
        ttlCache.onAccess("1");
        ttlCache.onAccess("2");

        // Wait for TTL to expire
        TimeUnit.MILLISECONDS.sleep(2500);

        ttlCache.clearExpiredCache();

        assertTrue(ttlCache.isExpired("1"));
        assertTrue(ttlCache.isExpired("2"));
        assertNull(ttlCache.evict()); // Should return null since all expired entries are cleared
    }

    @Test
    void testEvictReturnsNullIfNoExpiredEntries() {
        ttlCache.onAccess("1");
        ttlCache.onAccess("2");

        assertNull(ttlCache.evict()); // Since TTL hasn't expired yet
    }
}
