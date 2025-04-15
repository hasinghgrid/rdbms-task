package com.cachesystem.jdbc;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.ibatis.jdbc.ScriptRunner;

public class JDBCUtil {

    // Load from .env file
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static String getenv(String key) {
        String sys = System.getProperty(key);
        if (sys != null) return sys;
        String env = System.getenv(key);
        if (env != null) return env;
        return dotenv.get(key);  // from .env
    }

    private static final String DB_URL = getenv("DB_URL");
    private static final String DB_USER = getenv("DB_USER");
    private static final String DB_PASSWORD = getenv("DB_PASSWORD");


    // Get database connection
    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Execute a simple query (e.g., DDL or DML with no result)
    public static void execute(String query, Object... args) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            setParameters(stmt, args);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Execute a query with a PreparedStatement Consumer for more flexible usage
    public static void execute(String query, Consumer<PreparedStatement> consumer) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            consumer.accept(stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Find one record (useful for queries returning a single result)
    public static <T> T findOne(String query, Function<ResultSet, T> mapper, Object... args) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            setParameters(stmt, args);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapper.apply(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Find many records (useful for queries returning multiple results)
    public static <T> List<T> findMany(String query, Function<ResultSet, T> mapper, Object... args) {
        List<T> results = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            setParameters(stmt, args);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.apply(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // Helper method to set parameters on a PreparedStatement
    private static void setParameters(PreparedStatement stmt, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            stmt.setObject(i + 1, args[i]);
        }
    }

    // Execute an SQL script from a file (e.g., for creating indexes or schema migrations)
    public static void runSQLScript(String fileName) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            ScriptRunner runner = new ScriptRunner(conn);
            FileReader fileReader = new FileReader(fileName);
            runner.runScript(fileReader);
            conn.commit();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // Begin a transaction
    public static void beginTransaction() throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
        }
    }

    // Commit a transaction
    public static void commitTransaction() throws SQLException {
        try (Connection conn = getConnection()) {
            conn.commit();
        }
    }

    // Rollback a transaction
    public static void rollbackTransaction() throws SQLException {
        try (Connection conn = getConnection()) {
            conn.rollback();
        }
    }

    // Analyze a query and explain execution plan
    public static void explainQuery(String query) {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String explainQuery = "EXPLAIN ANALYZE " + query;
            ResultSet rs = stmt.executeQuery(explainQuery);
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create an index on a table
    public static void createIndex(String tableName, String indexName, String columnName) {
        String query = "CREATE INDEX IF NOT EXISTS " + indexName + " ON " + tableName + " (" + columnName + ")";
        execute(query);
        System.out.println("Index " + indexName + " created on table " + tableName);
    }
}
