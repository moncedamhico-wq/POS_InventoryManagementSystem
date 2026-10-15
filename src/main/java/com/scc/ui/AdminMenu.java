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
            System.out.println("2. Manage inventory   (coming soon)");
            System.out.println("3. Sales reports      (coming soon)");
            System.out.println("4. Logout");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> viewAllUsers();
                case "2" -> System.out.println("Manage inventory: not implemented yet.");
                case "3" -> System.out.println("Sales reports: not implemented yet.");
                case "4" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewAllUsers() {
        List<User> users = userDoa.getAllUsers();
        System.out.printf("%-5s %-15s %-25s %-8s%n", "ID", "Username", "Full name", "Role");
        System.out.println("-".repeat(56));
        for (User u : users) {
            System.out.printf("%-5d %-15s %-25s %-8s%n",
                    u.getId(), u.getUsername(), u.getFullName(), u.getRoles());
        }
    }
}