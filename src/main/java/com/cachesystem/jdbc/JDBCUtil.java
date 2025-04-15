package com.cachesystem.jdbc;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class JDBCUtil {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    private static String getenv(String key) {
        String sys = System.getProperty(key);
        if (sys != null) return sys;
        String env = System.getenv(key);
        if (env != null) return env;
        return dotenv.get(key);
    }

    private static final String DB_URL = getenv("DB_URL");
    private static final String DB_USERNAME = getenv("DB_USER");
    private static final String DB_PASSWORD = getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (DB_URL == null) {
            throw new RuntimeException("JDBCUtil Error: DB_URL is null. Check system properties, environment variables, or .env file.");
        }
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    public static void execute(String query, Object... args) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Query failed: " + query, e);
        }
    }

    public static void execute(String query, Consumer<PreparedStatement> consumer) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            consumer.accept(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Query failed: " + query, e);
        }
    }

    public static <T> T findOne(String query, Function<ResultSet, T> mapper, Object... args) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapper.apply(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Query failed: " + query, e);
        }
    }
    public static <T> List<T> findMany(String query, Function<ResultSet, T> mapper, Object... args) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            ResultSet rs = ps.executeQuery();
            List<T> results = new ArrayList<>();

            while (rs.next()) {
                results.add(mapper.apply(rs));
            }

            return results;
        } catch (SQLException e) {
            throw new RuntimeException("Query failed: " + query, e);
        }
    }

}
