-- ================================================
-- QUERY: Get all cache entries with their policies
-- ================================================
SELECT
    c.`key`,
    c.value,
    ep.name AS eviction_policy
FROM Cache c
JOIN EvictionPolicy ep ON c.eviction_policy_id = ep.id;

-- ================================================
-- QUERY: Count of cache entries per eviction policy
-- ================================================
SELECT
    ep.name AS eviction_policy,
    COUNT(c.id) AS cache_count
FROM EvictionPolicy ep
LEFT JOIN Cache c ON ep.id = c.eviction_policy_id
GROUP BY ep.name;

-- ================================================
-- QUERY: Cache entries with exceptions
-- ================================================
SELECT
    c.`key`,
    e.type,
    e.message
FROM Cache c
JOIN Exception e ON c.id = e.cache_id;

-- ================================================
-- QUERY: List cache managers and number of caches they manage
-- ================================================
SELECT
    cm.name AS cache_manager,
    COUNT(cc.cache_id) AS total_managed_caches
FROM CacheManager cm
LEFT JOIN CacheManager_Cache cc ON cm.id = cc.cache_manager_id
GROUP BY cm.name;

-- ================================================
-- QUERY: Show LRU, LFU, TTL policies and their base eviction policy name
-- ================================================
SELECT
    ep.name AS base_policy,
    'LRU' AS policy_type
FROM LruPolicy l
JOIN EvictionPolicy ep ON l.eviction_policy_id = ep.id

UNION ALL

SELECT
    ep.name,
    'LFU'
FROM LfuPolicy lf
JOIN EvictionPolicy ep ON lf.eviction_policy_id = ep.id

UNION ALL

SELECT
    ep.name,
    'TTL'
FROM TtlPolicy t
JOIN EvictionPolicy ep ON t.eviction_policy_id = ep.id;

-- ================================================
-- QUERY: Top 5 cache entries by exception frequency
-- ================================================
SELECT
    c.`key`,
    COUNT(e.id) AS exception_count
FROM Cache c
JOIN Exception e ON c.id = e.cache_id
GROUP BY c.id
ORDER BY exception_count DESC
LIMIT 5;
