package com.cachesystem.cache;

import com.cachesystem.eviction.EvictionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic Cache implementation using an eviction policy.
 */
public class GenericCache<K, V> implements Cache<K, V> {
    private final int capacity;
    private final Map<K, V> storage;
    private final EvictionPolicy<K> evictionPolicy;

    public GenericCache(int capacity, EvictionPolicy<K> evictionPolicy) {
        this.capacity = capacity;
        this.storage = new HashMap<>();
        this.evictionPolicy = evictionPolicy;
    }

    @Override
    public void put(K key, V value) {
        if (storage.size() >= capacity) {
            K evictedKey = evictionPolicy.evict();
            if (evictedKey != null) {
                storage.remove(evictedKey);
            }
        }
        storage.put(key, value);
        evictionPolicy.onAccess(key);
    }

    @Override
    public V get(K key) {
        if (!storage.containsKey(key)) {
            return null;
        }
        evictionPolicy.onAccess(key);
        return storage.get(key);
    }

    @Override
    public boolean isFull() {
        return storage.size() >= capacity;
    }
}
