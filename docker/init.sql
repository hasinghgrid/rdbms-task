-- Automatically executed by MySQL container at startup

USE cache_system;

-- Insert Eviction Policies
INSERT INTO EvictionPolicy (name, description) VALUES ('LRU', 'Least Recently Used eviction policy');
INSERT INTO EvictionPolicy (name, description) VALUES ('LFU', 'Least Frequently Used eviction policy');
INSERT INTO EvictionPolicy (name, description) VALUES ('TTL', 'Time-to-live eviction policy');

-- Link to sub-policy tables
INSERT INTO LruPolicy (eviction_policy_id) VALUES (1);
INSERT INTO LfuPolicy (eviction_policy_id) VALUES (2);
INSERT INTO TtlPolicy (eviction_policy_id) VALUES (3);

-- Cache Manager
INSERT INTO CacheManager (name) VALUES ('Default Manager');

-- Cache entries
INSERT INTO Cache (`key`, value, eviction_policy_id) VALUES ('cache:lru:1', 'LRU Test Data', 1);
INSERT INTO Cache (`key`, value, eviction_policy_id) VALUES ('cache:lfu:1', 'LFU Test Data', 2);
INSERT INTO Cache (`key`, value, eviction_policy_id) VALUES ('cache:ttl:1', 'TTL Test Data', 3);

-- Linking caches to manager
INSERT INTO CacheManager_Cache (cache_manager_id, cache_id) VALUES (1, 1);
INSERT INTO CacheManager_Cache (cache_manager_id, cache_id) VALUES (1, 2);
INSERT INTO CacheManager_Cache (cache_manager_id, cache_id) VALUES (1, 3);

-- Exceptions
INSERT INTO Exception (type, message, cache_id) VALUES ('CacheFullException', 'LRU cache is full', 1);
INSERT INTO Exception (type, message, cache_id) VALUES ('ItemNotFoundException', 'Item not found in LFU cache', 2);
