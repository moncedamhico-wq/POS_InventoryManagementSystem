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
            System.out.println("3. Update user role / status");
            System.out.println("4. Manage inventory   (coming soon)");
            System.out.println("5. Sales reports      (coming soon)");
            System.out.println("6. Logout");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> viewAllUsers();
                case "2" -> approvePendingUsers();
                case "3" -> updateUser();
                case "4" -> System.out.println("Manage inventory: not implemented yet.");
                case "5" -> System.out.println("Sales reports: not implemented yet.");
                case "6" -> running = false;
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

    // ===== Deliverable 7: UPDATE USER ROLE / STATUS =====

    private void updateUser() {
        // Display all users in a table
        List<User> users = userDoa.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("\n===== SELECT USER TO UPDATE =====");
        viewAllUsers();

        // Pick the user (0 cancels)
        int userId = readIntInput("Enter selected ID to update (or 0 to cancel): ");

        if (userId == 0) {
            System.out.println(" -> Operation Cancelled.");
            return;
        }

        // Self-edit restriction
        if (userId == admin.getId()) {
            System.out.println(" -> [WARNING] Modifying current active Admin is restricted");
            return;
        }

        // New role (keeps asking until valid, 0 cancels)
        String newRole = readValidRole();
        if (newRole == null) {
            System.out.println(" ->Operation Cancelled.");
            return;
        }

        // New status (keeps asking until valid, 0 cancels)
        String newStatus = readValidStatus();
        if (newStatus == null) {
            System.out.println(" ->Operation Cancelled.");
            return;
        }

        // Save (only the role and status change). The DAO prints the result message.
        userDoa.updateUserAndStatus(userId, newRole, newStatus);
    }

    // ===== Deliverable 7: INPUT VALIDATION =====

    // Keeps asking until a whole number is typed (letters never crash the program)
    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("-> [ERROR] Please enter a valid numeric ID.");
            }
        }
    }

    // Keeps asking until a valid role is chosen (1, 2 or the role name). Returns null if 0 is entered to cancel.
    private String readValidRole() {
        while (true) {
            System.out.print("Enter Role [1] USER [2] ADMIN [0 to cancel]: ");
            String role = scanner.nextLine().trim().toUpperCase();

            if (role.equals("0") || role.equals("CANCEL")) {
                return null;
            }
            if (role.equals("1") || role.equals("USER")) {
                return "USER";
            }
            if (role.equals("2") || role.equals("ADMIN")) {
                return "ADMIN";
            }
            System.out.println("-> [ERROR] Invalid role. Must be 1 (USER) or 2 (ADMIN).");
        }
    }

    // Keeps asking until a valid status is typed. Returns null if 0 is entered to cancel.
    private String readValidStatus() {
        while (true) {
            System.out.print("Enter Status (APPROVED / PENDING) [0 to cancel]: ");
            String status = scanner.nextLine().trim().toUpperCase();

            if (status.equals("0") || status.equals("CANCEL")) {
                return null;
            }
            if (status.equals("APPROVED") || status.equals("PENDING")) {
                return status;
            }
            System.out.println("-> [ERROR] Invalid status. Must be APPROVED or PENDING.");
        }
    }
}