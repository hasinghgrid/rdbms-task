package com.cachesystem.eviction;

public interface EvictionPolicy<K> {
    void onAccess(K key);
    K evict();
}
