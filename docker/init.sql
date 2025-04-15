CREATE TABLE IF NOT EXISTS cache_entries (
  id SERIAL PRIMARY KEY,
  cache_key VARCHAR(255),
  valueC VARCHAR(255),
  eviction_policy_id INT,
  last_accessed TIMESTAMP
);

INSERT INTO cache_entries (cache_key, valueC, eviction_policy_id, last_accessed)
VALUES ('item1', 'value1', 1, NOW());
