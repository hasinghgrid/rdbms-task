-- Create the EvictionPolicy table
CREATE TABLE IF NOT EXISTS EvictionPolicy (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE, -- LRU, LFU, TTL
    description TEXT
);

-- Create the LruPolicy table
CREATE TABLE IF NOT EXISTS LruPolicy (
    id INT AUTO_INCREMENT PRIMARY KEY,
    eviction_policy_id INT,
    FOREIGN KEY (eviction_policy_id) REFERENCES EvictionPolicy(id)
);

-- Create the LfuPolicy table
CREATE TABLE IF NOT EXISTS LfuPolicy (
    id INT AUTO_INCREMENT PRIMARY KEY,
    eviction_policy_id INT,
    FOREIGN KEY (eviction_policy_id) REFERENCES EvictionPolicy(id)
);

-- Create the TtlPolicy table
CREATE TABLE IF NOT EXISTS TtlPolicy (
    id INT AUTO_INCREMENT PRIMARY KEY,
    eviction_policy_id INT,
    FOREIGN KEY (eviction_policy_id) REFERENCES EvictionPolicy(id)
);

-- Create the Cache table
CREATE TABLE IF NOT EXISTS Cache (
    id INT AUTO_INCREMENT PRIMARY KEY,
    `key` VARCHAR(255) NOT NULL UNIQUE,
    value TEXT,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    eviction_policy_id INT,
    FOREIGN KEY (eviction_policy_id) REFERENCES EvictionPolicy(id)
);

-- Create the CacheManager table
CREATE TABLE IF NOT EXISTS CacheManager (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create the CacheManager_Cache table (Many-to-many relationship between CacheManager and Cache)
CREATE TABLE IF NOT EXISTS CacheManager_Cache (
    cache_manager_id INT,
    cache_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cache_manager_id, cache_id),
    FOREIGN KEY (cache_manager_id) REFERENCES CacheManager(id),
    FOREIGN KEY (cache_id) REFERENCES Cache(id)
);

-- Create the Exception table
CREATE TABLE IF NOT EXISTS Exception (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL, -- CacheFullException, ItemNotFoundException
    message TEXT,
    cache_id INT,
    FOREIGN KEY (cache_id) REFERENCES Cache(id)
);
