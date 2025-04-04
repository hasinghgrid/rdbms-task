-- Create database if not exists
CREATE DATABASE IF NOT EXISTS cache_system;
USE cache_system;

-- ===== Schema Section =====

CREATE TABLE IF NOT EXISTS EvictionPolicy (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS LruPolicy (
    id INT AUTO_INCREMENT PRIMARY KEY,
    eviction_policy_id INT,
    FOREIGN KEY (eviction_policy_id) REFERENCES EvictionPolicy(id)
);

CREATE TABLE IF NOT EXISTS LfuPolicy (
    id INT AUTO_INCREMENT PRIMARY KEY,
    eviction_policy_id INT,
    FOREIGN KEY (eviction_policy_id) REFERENCES EvictionPolicy(id)
);

CREATE TABLE IF NOT EXISTS TtlPolicy (
    id INT AUTO_INCREMENT PRIMARY KEY,
    eviction_policy_id INT,
    FOREIGN KEY (eviction_policy_id) REFERENCES EvictionPolicy(id)
);

CREATE TABLE IF NOT EXISTS Cache (
    id INT AUTO_INCREMENT PRIMARY KEY,
    `key` VARCHAR(100) NOT NULL,
    value TEXT,
    eviction_policy_id INT,
    FOREIGN KEY (eviction_policy_id) REFERENCES EvictionPolicy(id)
);

CREATE TABLE IF NOT EXISTS CacheManager (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS CacheManager_Cache (
    cache_manager_id INT,
    cache_id INT,
    FOREIGN KEY (cache_manager_id) REFERENCES CacheManager(id),
    FOREIGN KEY (cache_id) REFERENCES Cache(id)
);

CREATE TABLE IF NOT EXISTS Exception (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(100),
    message TEXT,
    cache_id INT,
    FOREIGN KEY (cache_id) REFERENCES Cache(id)
);

CREATE TABLE IF NOT EXISTS cache_entries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    key_name VARCHAR(100),
    value TEXT,
    created_at DATETIME,
    expires_at DATETIME,
    eviction_policy VARCHAR(50)
);