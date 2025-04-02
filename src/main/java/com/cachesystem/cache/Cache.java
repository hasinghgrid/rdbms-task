package com.cachesystem.cache;

/**
 * Generic Cache Interface defining basic cache operations.
 */
public interface Cache<K, V> {
    void put(K key, V value);
    V get(K key);
    boolean isFull();
}
