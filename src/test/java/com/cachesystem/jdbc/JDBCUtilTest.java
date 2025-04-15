package com.cachesystem.jdbc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class JDBCUtilTest {

    // Before all tests, set up the H2 in-memory database and the schema
    @BeforeAll
    public static void setUp() throws SQLException {
        // Set up the database URL, user, and password for H2
        // Use H2 in-memory DB
        System.setProperty("DB_URL", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        System.setProperty("DB_USER", "sa");
        System.setProperty("DB_PASSWORD", "");

        // Run initial schema creation for cache_entries table
        JDBCUtil.execute("CREATE TABLE IF NOT EXISTS cache_entries (id INT PRIMARY KEY, cache_key VARCHAR(255), last_accessed TIMESTAMP)");
    }

    @BeforeEach
    public void clearTable() throws SQLException {
        JDBCUtil.execute("DELETE FROM cache_entries");
    }

    @Test
    public void testExecuteUpdate() throws SQLException {
        // Test simple DDL/DML query execution (INSERT)
        JDBCUtil.execute("INSERT INTO cache_entries (id, cache_key, last_accessed) VALUES (?, ?, ?)", 1, "key1", "2025-04-01 10:00:00");

        // Verify the record is inserted
        String result = JDBCUtil.findOne("SELECT cache_key FROM cache_entries WHERE id = 1", rs -> {
            try {
                return rs.getString("cache_key");
            } catch (SQLException e) {
                return null;
            }
        });

        assertEquals("key1", result);
    }

    @Test
    public void testFindOne() throws SQLException {
        // Insert test data
        JDBCUtil.execute("INSERT INTO cache_entries (id, cache_key, last_accessed) VALUES (?, ?, ?)", 2, "key2", "2025-04-02 10:00:00");

        // Test findOne() method to retrieve the cache_key
        String result = JDBCUtil.findOne("SELECT cache_key FROM cache_entries WHERE id = 2", rs -> {
            try {
                return rs.getString("cache_key");
            } catch (SQLException e) {
                return null;
            }
        });

        assertEquals("key2", result);
    }

    @Test
    void testFindMany() throws SQLException {
        // Use cache_entries for the test
        JDBCUtil.execute("INSERT INTO cache_entries (id, cache_key, last_accessed) VALUES (?, ?, ?)", 1, "key1", "2025-04-01 10:00:00");
        JDBCUtil.execute("INSERT INTO cache_entries (id, cache_key, last_accessed) VALUES (?, ?, ?)", 2,"key2", "2025-04-02 10:00:00");
        JDBCUtil.execute("INSERT INTO cache_entries (id, cache_key, last_accessed) VALUES (?, ?, ?)", 3,"key3", "2025-04-03 10:00:00");

        List<String> keys = JDBCUtil.findMany(
                "SELECT cache_key FROM cache_entries",
                rs -> {
                    try {
                        return rs.getString("cache_key");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        assertEquals(3, keys.size(), "Expected 3 cache keys to be returned");
        assertTrue(keys.contains("key1"));
        assertTrue(keys.contains("key2"));
        assertTrue(keys.contains("key3"));
    }

    @Test
    void testRunSQLScript() throws SQLException {
        // First, run table creation script
        JDBCUtil.runSQLScript("src/main/resources/sql/create_tables.sql");

        // Then run index creation script
        JDBCUtil.runSQLScript("src/main/resources/sql/create_indexes.sql");

        // Now verify the index was created (H2 uppercases identifiers unless quoted)
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM INFORMATION_SCHEMA.INDEXES WHERE INDEX_NAME = ?")) {

            ps.setString(1, "IDX_UNIQUE_CACHE_KEY");
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "ResultSet should not be empty");
                assertEquals(1, rs.getInt(1), "The index 'IDX_UNIQUE_CACHE_KEY' should exist");
            }
        }
    }

    @Test
    public void testTransactionCommit() throws SQLException {
        // Begin transaction
        JDBCUtil.beginTransaction();
        JDBCUtil.execute("INSERT INTO cache_entries (id, cache_key, last_accessed) VALUES (?, ?, ?)", 5, "key5", "2025-04-05 10:00:00");

        // Commit transaction
        JDBCUtil.commitTransaction();

        // Verify the inserted record
        String result = JDBCUtil.findOne("SELECT cache_key FROM cache_entries WHERE id = 5", rs -> {
            try {
                return rs.getString("cache_key");
            } catch (SQLException e) {
                return null;
            }
        });

        assertEquals("key5", result);
    }

    @Test
    void testTransactionRollback() throws SQLException {
        try (Connection conn = JDBCUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO cache_entries (id, cache_key, last_accessed) VALUES (?, ?, ?)")) {
                ps.setInt(1, 6); // ID
                ps.setString(2, "key6"); // cache_key
                ps.setString(3, "2025-04-06 10:00:00"); // last_accessed
                ps.executeUpdate();
            }

            conn.rollback(); // Cancel the insert

            // Validate rollback: item should not be found
            String result = JDBCUtil.findOne(
                    "SELECT cache_key FROM cache_entries WHERE cache_key = ?",
                    rs -> {
                        try {
                            return rs.getString("cache_key");
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    "key6"
            );

            assertNull(result, "key6 should not exist after rollback");
        }
    }


    @Test
    public void testTransactionIsolation() throws SQLException {
        // Set isolation level for transaction
        try (Connection conn = JDBCUtil.getConnection()) {
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            // Begin transaction with SERIALIZABLE isolation
            JDBCUtil.beginTransaction();

            JDBCUtil.execute("INSERT INTO cache_entries (id, cache_key, last_accessed) VALUES (?, ?, ?)", 7, "key7", "2025-04-07 10:00:00");

            // Verify the isolation level has been set
            assertEquals(Connection.TRANSACTION_SERIALIZABLE, conn.getTransactionIsolation());

            JDBCUtil.commitTransaction();
        }

        // Verify the record has been committed
        String result = JDBCUtil.findOne("SELECT cache_key FROM cache_entries WHERE id = 7", rs -> {
            try {
                return rs.getString("cache_key");
            } catch (SQLException e) {
                return null;
            }
        });

        assertEquals("key7", result);
    }
}
