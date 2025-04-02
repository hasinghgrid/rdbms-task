package com.cachesystem.cache;

import com.cachesystem.eviction.LRUCache;
import com.cachesystem.eviction.LFUCache;
import com.cachesystem.eviction.TTLCache;
import com.cachesystem.exception.InvalidCacheTypeException;

/**
 * Factory class to create cache instances based on eviction policy.
 */
public class CacheFactory {
    public static <K, V> Cache<K, V> createCache(int capacity, String type, long ttl) {
        switch (type.toUpperCase()) {
            case "LRU":
                return new GenericCache<>(capacity, new LRUCache<>(capacity));
            case "LFU":
                return new GenericCache<>(capacity, new LFUCache<>(capacity));
            case "TTL":
                return new GenericCache<>(capacity, new TTLCache<>(ttl));
            default:
                throw new InvalidCacheTypeException("Invalid cache type: " + type);
        }
    }
}
