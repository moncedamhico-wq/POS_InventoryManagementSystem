package com.scc.doa;

import com.scc.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDoa {

    public boolean register(User user) {
        if (usernameExists(user.getUsername())) {
            return false;
        }
        // Stores plain text password directly
        String sql = "INSERT INTO Users (username, password, full_name, roles, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getRoles());
            ps.setString(5, user.getStatus() != null ? user.getStatus() : "PENDING");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Register error: " + e.getMessage());
            return false;
        }
    }

    public User login(String username, String password) {
        // Compares plain text password directly
        String sql = "SELECT id, username, password, full_name, roles, status FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM Users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, password, full_name, roles, status FROM Users ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                users.add(map(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return users;
    }

    public List<User> getPendingUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, password, full_name, roles, status FROM Users WHERE status = 'PENDING' ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                users.add(map(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return users;
    }

    public boolean approveUser(int userId) {
        String sql = "UPDATE Users SET status = 'APPROVED' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Approve error: " + e.getMessage());
            return false;
        }
    }

    private User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("full_name"),
                rs.getString("roles"),
                rs.getString("status")
        );
    }
}