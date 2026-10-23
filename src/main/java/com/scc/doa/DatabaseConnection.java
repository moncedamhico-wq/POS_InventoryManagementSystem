package com.scc.doa;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_FILE = "POS_InventoryManagement.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC"); // makes a missing driver obvious
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite driver not found - reload Maven", e);
        }
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        System.out.println("Using database: " + new File(DB_FILE).getAbsolutePath());
        String sql = """
                CREATE TABLE IF NOT EXISTS Users (
                    id INTEGER PRIMARY KEY,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    full_name TEXT NOT NULL,
                    roles TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'PENDING'
                )
                """;
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}