package com.cachesystem.jdbc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JDBCUtilTest {

    @BeforeAll
    static void setup() {
        // Use H2 in-memory DB
        System.setProperty("DB_URL", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        System.setProperty("DB_USER", "sa");
        System.setProperty("DB_PASSWORD", "");

        JDBCUtil.execute(
                "CREATE TABLE IF NOT EXISTS cache_entries (" +
                        "id SERIAL PRIMARY KEY, " +
                        "cache_key VARCHAR(255), " +
                        "valueC VARCHAR(255), " +
                        "eviction_policy_id INT, " +
                        "last_accessed TIMESTAMP" +
                        ")"
        );

        JDBCUtil.execute(
                "INSERT INTO cache_entries (cache_key, valueC , eviction_policy_id, last_accessed) " +
                        "VALUES (?, ?, ?, ?)",
                "item1", "value1", 1, new Timestamp(System.currentTimeMillis())
        );
    }

    @Test
    void testExecuteWithArgs() {
        JDBCUtil.execute(
                "INSERT INTO cache_entries (cache_key, valueC, eviction_policy_id, last_accessed) " +
                        "VALUES (?, ?, ?, ?)",
                "B", "Value3", 3, new Timestamp(System.currentTimeMillis())
        );

        var entry = JDBCUtil.findOne(
                "SELECT valueC FROM cache_entries WHERE cache_key = ?",
                rs -> {
                    try {
                        return rs.getString("valueC");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                "B"
        );

        assertEquals("Value3", entry);
    }

    @Test
    void testExecuteWithConsumer() {
        JDBCUtil.execute(
                "INSERT INTO cache_entries (cache_key, valueC, eviction_policy_id, last_accessed) VALUES (?, ?, ?, ?)",
                ps -> {
                    try {
                        ps.setString(1, "C");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        ps.setString(2, "Value4");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        ps.setInt(3, 4);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        var entry = JDBCUtil.findOne(
                "SELECT valueC FROM cache_entries WHERE cache_key = ?",
                rs -> {
                    try {
                        return rs.getString("valueC");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                "C"
        );

        assertEquals("Value4", entry);
    }

    @Test
    void testFindOne() {
        JDBCUtil.execute(
                "INSERT INTO cache_entries (cache_key, valueC, eviction_policy_id, last_accessed) " +
                        "VALUES (?, ?, ?, ?)",
                "D", "Value5", 5, new Timestamp(System.currentTimeMillis())
        );

        var entry = JDBCUtil.findOne(
                "SELECT valueC FROM cache_entries WHERE cache_key = ?",
                rs -> {
                    try {
                        return rs.getString("valueC");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                "D"
        );

        assertEquals("Value5", entry);
    }

    @Test
    void testFindMany() {
        JDBCUtil.execute(
                "INSERT INTO cache_entries (cache_key, valueC, eviction_policy_id, last_accessed) VALUES (?, ?, ?, ?)",
                "many1", "v1", 2, new Timestamp(System.currentTimeMillis())
        );

        JDBCUtil.execute(
                "INSERT INTO cache_entries (cache_key, valueC, eviction_policy_id, last_accessed) VALUES (?, ?, ?, ?)",
                "many2", "v2", 2, new Timestamp(System.currentTimeMillis())
        );

        List<String> values = JDBCUtil.findMany(
                "SELECT valueC FROM cache_entries WHERE eviction_policy_id = ? ORDER BY cache_key",
                rs -> {
                    try {
                        return rs.getString("valueC");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                2
        );

        assertEquals(2, values.size());
        assertTrue(values.contains("v1"));
        assertTrue(values.contains("v2"));
    }

    @Test
    void testFindManyReturnsEmptyListWhenNoMatch() {
        List<String> result = JDBCUtil.findMany(
                "SELECT valueC FROM cache_entries WHERE eviction_policy_id = ?",
                rs -> {
                    try {
                        return rs.getString("valueC");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                -99 // no such ID
        );

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
