-- Creating an index on cache_key column for fast retrieval of cache items by key
CREATE INDEX idx_cache_key ON cache_entries (cache_key);

-- Creating an index on last_accessed timestamp for LRU (Least Recently Used) eviction policy
CREATE INDEX idx_cache_last_accessed ON cache_entries (last_accessed);

-- Creating an index on access_count for LFU (Least Frequently Used) eviction policy
CREATE INDEX idx_cache_access_count ON cache_entries (access_count);

-- Creating a composite index on cache_key and last_accessed for faster lookups by both key and last accessed
CREATE INDEX idx_cache_key_last_accessed ON cache_entries (cache_key, last_accessed);

-- Creating an index for TTL expiration, assuming 'expires_at' holds the time-to-live value
CREATE INDEX idx_cache_expires_at ON cache_entries (expires_at);

-- Creating an index on access_count and cache_key for LFU eviction on frequently accessed items
CREATE INDEX idx_cache_access_count_key ON cache_entries (access_count, cache_key);

-- Creating a unique index on cache_key to ensure no duplicate cache entries
CREATE UNIQUE INDEX idx_unique_cache_key ON cache_entries (cache_key);
