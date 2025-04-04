-- ================================
-- FULL CRUD FOR CACHE ENTITY
-- ================================

-- CREATE
INSERT INTO Cache (`key`, value, eviction_policy_id) VALUES ('cache:new:123', 'New Data', 1);

-- READ
SELECT * FROM Cache WHERE `key` = 'cache:new:123';

-- UPDATE
UPDATE Cache SET value = 'Updated Data' WHERE `key` = 'cache:new:123';

-- DELETE
DELETE FROM Cache WHERE `key` = 'cache:new:123';
-- ================================
-- SEARCH QUERY WITH FILTERS, PAGINATION, SORTING
-- ================================

-- Search cache entries by eviction policy (e.g., 'LRU')
SELECT c.id, c.`key`, c.value, ep.name AS eviction_policy
FROM Cache c
JOIN EvictionPolicy ep ON c.eviction_policy_id = ep.id
WHERE ep.name = 'LRU'
ORDER BY c.id DESC
LIMIT 5 OFFSET 0;

-- ================================
-- JOINED DATA QUERY (REAL USE CASE)
-- ================================

-- Fetch cache details along with eviction policy name and associated CacheManager (if any)
SELECT
  c.`key`,
  c.value,
  ep.name AS eviction_policy,
  cm.name AS cache_manager_name
FROM Cache c
JOIN EvictionPolicy ep ON c.eviction_policy_id = ep.id
LEFT JOIN CacheManager_Cache cmc ON c.id = cmc.cache_id
LEFT JOIN CacheManager cm ON cm.id = cmc.cache_manager_id;


-- ================================
-- STATISTICS QUERY
-- ================================

-- Count of cache entries grouped by eviction policy
SELECT
  ep.name AS eviction_policy,
  COUNT(*) AS total_cache_entries
FROM Cache c
JOIN EvictionPolicy ep ON c.eviction_policy_id = ep.id
GROUP BY ep.name;

-- ================================
-- TOP-N QUERY
-- ================================

-- Top 3 eviction policies with most cache entries
SELECT
  ep.name AS eviction_policy,
  COUNT(*) AS cache_count
FROM Cache c
JOIN EvictionPolicy ep ON c.eviction_policy_id = ep.id
GROUP BY ep.name
ORDER BY cache_count DESC
LIMIT 3;
