package com.cachesystem.eviction;

import java.util.*;

public class TTLCache<K, V> implements EvictionPolicy<K> {
    private final Map<K, V> storage = new HashMap<>();
    private final Map<K, Long> timestampMap = new HashMap<>();
    private final long ttl;

    public TTLCache(long ttl) {
        this.ttl = ttl;
    }

    @Override
    public void onAccess(K key) {
        timestampMap.put(key, System.currentTimeMillis());
    }

    @Override
    public K evict() {
        long currentTime = System.currentTimeMillis();

        // Find the oldest expired entry
        Optional<Map.Entry<K, Long>> expiredEntry = timestampMap.entrySet().stream()
                .filter(entry -> currentTime - entry.getValue() > ttl)
                .min(Comparator.comparingLong(Map.Entry::getValue));

        if (expiredEntry.isPresent()) {
            K keyToEvict = expiredEntry.get().getKey();
            timestampMap.remove(keyToEvict);
            storage.remove(keyToEvict);
            return keyToEvict;
        }

        return null;
    }

    // Method to check if an item is expired
    public boolean isExpired(K key) {
        long currentTime = System.currentTimeMillis();
        return (currentTime - timestampMap.getOrDefault(key, 0L)) > ttl;
    }

    public void clearExpiredCache() {
        long currentTime = System.currentTimeMillis();
        timestampMap.entrySet().removeIf(entry -> currentTime - entry.getValue() > ttl);
    }

    // Method to add items to the cache
    public void put(K key, V value) {
        storage.put(key, value); // Add item to storage
        onAccess(key); // Update timestamp on access
    }

    // Method to get items from the cache
    public V get(K key) {
        if (!storage.containsKey(key)) {
            return null; // Key not found
        }
        onAccess(key); // Update timestamp on access
        return storage.get(key); // Return value from storage
    }
}
