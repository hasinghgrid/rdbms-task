package com.cachesystem;

import com.cachesystem.cache.CacheFactory;
import com.cachesystem.exception.InvalidCacheTypeException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CacheTest {
    @Test
    void testInvalidCacheTypeException() {
        Exception exception = assertThrows(InvalidCacheTypeException.class,
                () -> CacheFactory.createCache(2, "INVALID", 0));
        assertEquals("Invalid cache type: INVALID", exception.getMessage());
    }
}
