# RDBMS Practical Tasks

This document presents practical tasks completed using **JDBC**, **PostgreSQL**, and domain-specific relational models. It demonstrates how relational database principles apply to real-world systems using transaction management, isolation levels, indexing, and cross-database compatibility challenges.

---

## ✅ Task 1: Demonstrating Database Inconsistency (Breaking the "C" in ACID)

A test was implemented to simulate a violation of consistency by performing an update operation without proper transaction handling. The test includes:

- **Before**: A valid value is inserted into the table.
- **During**: A new value is inserted but an exception is thrown before commit.
- **After**: The test checks whether the new value was rolled back or not.

### 🔍 Observation:

- **With transaction + rollback**: The database remained consistent, and the intermediate state was not persisted.
- **Without transaction**: The intermediate state remained in the database, leading to **inconsistency**.

> See: `JDBCUtilTest#testTransactionRollback`

---

## ✅ Task 2: Transaction Isolation Level Demonstration

This task evaluates how **transaction isolation levels** affect concurrent operations. Two threads were used:

- **Writer**: Modifies a record.
- **Reader**: Reads the same record concurrently.

### Scenarios Tested:

1. **Default Isolation Level (READ_COMMITTED)**:
    - The reader might observe different results if the writer commits changes between reads.

2. **Higher Isolation Level (REPEATABLE_READ)**:
    - The reader consistently sees the same data throughout the transaction.

### 🔍 Observation:

Using `REPEATABLE_READ` avoids **non-repeatable reads**, ensuring the cache layer or application logic behaves deterministically during concurrent transactions.

---

## ✅ Task 3: Indexing for Performance Optimization

To evaluate the impact of **indexes** on query performance:

1. A table was populated with **millions of rows** using a script.
2. Queries were executed:
    - Before creating an index
    - After creating an index on the search column

### Tools Used:

- PostgreSQL `EXPLAIN ANALYZE`
- Timer-based query benchmarking

### 🔍 Observation:

- Queries without an index resulted in **sequential scans**, taking seconds.
- After index creation, queries completed in **milliseconds** using **index scans**.
- Indexes significantly improve performance for lookup-intensive operations.

> Index used: `CREATE INDEX idx_cache_key ON cache_table(cache_key);`

---

## ✅ Task 4: Compound (Multi-Column) Index

A **compound index** was created on multiple columns (e.g., `eviction_policy_id`, `last_accessed`).

### Test Cases:

- Queries using **both columns** from the compound index
- Queries using **only the first column**
- Queries using **only the second column**

### 🔍 Observation:

- Full index usage provided best performance.
- Partial usage (on first column only) was acceptable.
- Queries using only the second column **did not benefit** from the compound index.

> Index created:  
> `CREATE INDEX idx_policy_access ON cache_table(eviction_policy_id, last_accessed);`

---

## ⚠️ Task 5: Handling H2 vs PostgreSQL Compatibility

While integrating index creation and schema definitions, certain issues were encountered in **H2** that do not occur in **PostgreSQL**:

### ❌ Issues Encountered in H2:

- `AUTO_INCREMENT` syntax caused errors.  
  ✅ Fix: Replaced with `IDENTITY` (H2-compatible auto-increment).

- Index creation failed due to:
    - Table not created correctly beforehand.
    - Column names being misinterpreted as missing.

- Example errors:
    - `Syntax error in SQL statement [...] expected "identifier"`
    - `Column "ACCESS_COUNT" not found`
    - `Column "EXPIRES_AT" not found`

### ✅ Solution:

- Ensured **table creation is error-free** before running index scripts.
- Updated DDL to use **H2-compatible syntax** in tests.
- Used conditional logic in test scripts to accommodate database-specific syntax differences.

---

## ✅ Code Coverage
