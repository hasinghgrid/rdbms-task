-- Insert example eviction policies
INSERT INTO EvictionPolicy (name, description) VALUES ('LRU', 'Least Recently Used eviction policy');
INSERT INTO EvictionPolicy (name, description) VALUES ('LFU', 'Least Frequently Used eviction policy');
INSERT INTO EvictionPolicy (name, description) VALUES ('TTL', 'Time-to-live based eviction policy');

-- Insert an LRU policy linked to the LRU eviction policy
INSERT INTO LruPolicy (eviction_policy_id) VALUES (1);

-- Insert a cache with an eviction policy
INSERT INTO Cache (`key`, value, eviction_policy_id) VALUES ('user:1001', 'John Doe', 1);

-- Insert a Cache Manager
INSERT INTO CacheManager (name) VALUES ('Main Cache Manager');

-- Link a cache to the Cache Manager
INSERT INTO CacheManager_Cache (cache_manager_id, cache_id) VALUES (1, 1);

-- Insert an exception related to a cache
INSERT INTO Exception (type, message, cache_id) VALUES ('CacheFullException', 'Cache has exceeded its size limit', 1);

-- Fetch all caches with their eviction policies
SELECT Cache.`key`, Cache.value, EvictionPolicy.name AS eviction_policy
FROM Cache
JOIN EvictionPolicy ON Cache.eviction_policy_id = EvictionPolicy.id;

-- Fetch all caches managed by a specific Cache Manager
SELECT Cache.`key`, Cache.value
FROM Cache
JOIN CacheManager_Cache ON Cache.id = CacheManager_Cache.cache_id
WHERE CacheManager_Cache.cache_manager_id = 1;

-- Get all exceptions related to a cache
SELECT Exception.type, Exception.message
FROM Exception
WHERE Exception.cache_id = 1;

-- Fetch caches by eviction policy type (e.g., LRU)
SELECT Cache.`key`, Cache.value
FROM Cache
JOIN EvictionPolicy ON Cache.eviction_policy_id = EvictionPolicy.id
WHERE EvictionPolicy.name = 'LRU';

-- Count the number of caches under a Cache Manager
SELECT CacheManager.name, COUNT(CacheManager_Cache.cache_id) AS cache_count
FROM CacheManager
JOIN CacheManager_Cache ON CacheManager.id = CacheManager_Cache.cache_manager_id
GROUP BY CacheManager.id;
