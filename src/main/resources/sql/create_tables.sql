CREATE TABLE IF NOT EXISTS cache_entries (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cache_key VARCHAR(255) NOT NULL,
    value VARCHAR(255),
    last_accessed TIMESTAMP,
    access_count INT DEFAULT 0,
    expires_at TIMESTAMP
);
