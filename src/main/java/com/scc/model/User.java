package com.scc.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String roles;
    private String status;

    public User() {}

    public User(int id, String username, String password, String fullName, String roles, String status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.roles = roles;
        this.status = status;
    }

    public User(String username, String password, String fullName, String roles) {
        this(0, username, password, fullName, roles, "PENDING");
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isAdmin() { return "ADMIN".equalsIgnoreCase(roles); }
}