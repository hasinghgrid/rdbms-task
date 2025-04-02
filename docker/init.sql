-- Initial database schema setup (if needed, can be left empty)
-- It will automatically run when the MySQL container starts

-- Example of initial data setup
INSERT INTO Cache (name, cache_type, eviction_policy) VALUES ('LRU Cache', 'LRU', 'LRU');
INSERT INTO Cache (name, cache_type, eviction_policy) VALUES ('LFU Cache', 'LFU', 'LFU');
