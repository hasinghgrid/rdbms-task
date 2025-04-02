package com.cachesystem.eviction;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU (Least Recently Used) eviction policy.
 */
public class LRUCache<K> implements EvictionPolicy<K> {
    private final Map<K, Boolean> accessOrderMap;

    public LRUCache(int capacity) {
        this.accessOrderMap = new LinkedHashMap<>(capacity, 0.75f, true);
    }

    @Override
    public void onAccess(K key) {
        accessOrderMap.put(key, true);
    }

    @Override
    public K evict() {
        if (accessOrderMap.isEmpty()) {
            return null;
        }
        K leastUsedKey = accessOrderMap.keySet().iterator().next();
        accessOrderMap.remove(leastUsedKey);
        return leastUsedKey;
    }
}
