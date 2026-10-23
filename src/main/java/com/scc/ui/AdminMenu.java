package com.scc.ui;

import com.scc.doa.UserDoa;
import com.scc.model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final Scanner scanner;
    private final UserDoa userDoa;
    private final User admin;

    public AdminMenu(Scanner scanner, UserDoa userDoa, User admin) {
        this.scanner = scanner;
        this.userDoa = userDoa;
        this.admin = admin;
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n===== ADMIN MENU (" + admin.getFullName() + ") =====");
            System.out.println("1. View all users");
            System.out.println("2. Approve pending users");
            System.out.println("3. Manage inventory   (coming soon)");
            System.out.println("4. Sales reports      (coming soon)");
            System.out.println("5. Logout");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> viewAllUsers();
                case "2" -> approvePendingUsers();
                case "3" -> System.out.println("Manage inventory: not implemented yet.");
                case "4" -> System.out.println("Sales reports: not implemented yet.");
                case "5" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewAllUsers() {
        List<User> users = userDoa.getAllUsers();
        System.out.printf("%-5s %-15s %-25s %-8s %-10s%n", "ID", "Username", "Full name", "Role", "Status");
        System.out.println("-".repeat(67));
        for (User u : users) {
            System.out.printf("%-5d %-15s %-25s %-8s %-10s%n",
                    u.getId(), u.getUsername(), u.getFullName(), u.getRoles(), u.getStatus());
        }
    }

    private void approvePendingUsers() {
        List<User> pendingUsers = userDoa.getPendingUsers();
        if (pendingUsers.isEmpty()) {
            System.out.println("No pending users to approve.");
            return;
        }

        System.out.println("\n===== PENDING USERS =====");
        System.out.printf("%-5s %-15s %-25s %-8s%n", "ID", "Username", "Full name", "Role");
        System.out.println("-".repeat(56));
        for (User u : pendingUsers) {
            System.out.printf("%-5d %-15s %-25s %-8s%n",
                    u.getId(), u.getUsername(), u.getFullName(), u.getRoles());
        }

        System.out.print("Enter User ID to approve (or 0 to cancel): ");
        String input = scanner.nextLine().trim();
        try {
            int userId = Integer.parseInt(input);
            if (userId == 0) return;

            boolean success = userDoa.approveUser(userId);
            if (success) {
                System.out.println("User successfully approved!");
            } else {
                System.out.println("Failed to approve user. Check ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }
}