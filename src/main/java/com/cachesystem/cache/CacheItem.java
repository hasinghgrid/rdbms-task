package com.cachesystem.cache;

/**
 * Represents an item in the cache with its key, value, and metadata (like timestamp).
 */
public class CacheItem<K, V> {
    private K key;
    private V value;
    private long timestamp;

    // Constructor to create CacheItem with key, value, and timestamp
    public CacheItem(K key, V value) {
        this.key = key;
        this.value = value;
        this.timestamp = System.currentTimeMillis(); // Set the timestamp when the item is created
    }

    // Getter for key
    public K getKey() {
        return key;
    }

    // Setter for key
    public void setKey(K key) {
        this.key = key;
    }

    // Getter for value
    public V getValue() {
        return value;
    }

    // Setter for value
    public void setValue(V value) {
        this.value = value;
    }

    // Getter for timestamp
    public long getTimestamp() {
        return timestamp;
    }

    // Setter for timestamp
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
