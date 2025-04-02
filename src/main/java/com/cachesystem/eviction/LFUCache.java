package com.cachesystem.eviction;

import java.util.HashMap;
import java.util.Map;

/**
 * LFU (Least Frequently Used) eviction policy.
 */
public class LFUCache<K> implements EvictionPolicy<K> {
    private final Map<K, Integer> frequencyMap;

    public LFUCache(int capacity) {
        this.frequencyMap = new HashMap<>();
    }

    @Override
    public void onAccess(K key) {
        frequencyMap.put(key, frequencyMap.getOrDefault(key, 0) + 1);
    }

    @Override
    public K evict() {
        if (frequencyMap.isEmpty()) {
            return null;
        }
        return frequencyMap.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
