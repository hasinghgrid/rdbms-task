-- Creating the main table for cache entries
CREATE TABLE cache_entries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    key_name VARCHAR(255) UNIQUE NOT NULL,
    value TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    eviction_policy ENUM('LRU', 'LFU', 'TTL') NOT NULL
);

-- CRUD Queries
-- 1. Insert a new cache entry
INSERT INTO cache_entries (key_name, value, expires_at, eviction_policy)
VALUES ('user_session', 'session_data', DATE_ADD(NOW(), INTERVAL 1 HOUR), 'LRU');

-- 2. Read a cache entry by key
SELECT * FROM cache_entries WHERE key_name = 'user_session';

-- 3. Update a cache entry
UPDATE cache_entries
SET value = 'updated_data', expires_at = DATE_ADD(NOW(), INTERVAL 2 HOUR)
WHERE key_name = 'user_session';

-- 4. Delete a cache entry
DELETE FROM cache_entries WHERE key_name = 'user_session';

-- Search Query with dynamic filters, pagination, and sorting
SELECT * FROM cache_entries
WHERE eviction_policy = 'LRU'
ORDER BY created_at DESC
LIMIT 10 OFFSET 0; -- Pagination with 10 results per page

-- Search Query with Joined Data
-- Assuming we have a 'cache_metadata' table for additional details
CREATE TABLE cache_metadata (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cache_entry_id INT,
    metadata JSON,
    FOREIGN KEY (cache_entry_id) REFERENCES cache_entries(id) ON DELETE CASCADE
);

SELECT ce.*, cm.metadata
FROM cache_entries ce
LEFT JOIN cache_metadata cm ON ce.id = cm.cache_entry_id
WHERE ce.eviction_policy = 'LFU';

-- Statistic Query: Total cache entries per eviction policy
SELECT eviction_policy, COUNT(*) AS total_entries
FROM cache_entries
GROUP BY eviction_policy;

-- Top-Something Query: Most frequently stored cache keys
SELECT key_name, COUNT(*) AS usage_count
FROM cache_entries
GROUP BY key_name
ORDER BY usage_count DESC
LIMIT 5;
