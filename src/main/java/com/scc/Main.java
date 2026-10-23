package com.scc;

import com.scc.doa.DatabaseConnection;
import com.scc.doa.UserDoa;
import com.scc.model.User;
import com.scc.ui.AdminMenu;
import com.scc.ui.UserMenu;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDoa userDoa = new UserDoa();

    public static void main(String[] args) {
        DatabaseConnection.initialize();

        boolean running = true;
        while (running) {
            System.out.println("\n===== POS & INVENTORY MANAGEMENT =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> login();
                case "2" -> register();
                case "3" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = userDoa.login(username, password);
        if (user == null) {
            System.out.println("Invalid username or password.");
            return;
        }

        if ("PENDING".equalsIgnoreCase(user.getStatus())) {
            System.out.println("Your account is pending admin approval.");
            return;
        }

        System.out.println("Welcome, " + user.getFullName() + "!");
        if (user.isAdmin()) {
            new AdminMenu(scanner, userDoa, user).show();
        } else {
            new UserMenu(scanner, user).show();
        }
    }

    private static void register() {
        System.out.print("Full name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Role (1 = User, 2 = Admin): ");

        String roleChoice = scanner.nextLine().trim();
        String roles = roleChoice.equals("2") ? "ADMIN" : "USER";

        // Admins are automatically APPROVED, regular Users start as PENDING
        String status = "ADMIN".equals(roles) ? "APPROVED" : "PENDING";

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            System.out.println("All fields are required.");
            return;
        }

        User user = new User(0, username, password, fullName, roles, status);
        if (userDoa.register(user)) {
            if ("ADMIN".equals(roles)) {
                System.out.println("Admin registration successful! You can now log in immediately.");
            } else {
                System.out.println("Registration successful! Your account is pending admin approval.");
            }
        } else {
            System.out.println("Registration failed (username may already exist).");
        }
    }
}