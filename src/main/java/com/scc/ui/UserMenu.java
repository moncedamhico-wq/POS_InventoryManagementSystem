package com.scc.ui;

import com.scc.model.User;

import java.util.Scanner;

public class UserMenu {
    private final Scanner scanner;
    private final User user;

    public UserMenu(Scanner scanner, User user) {
        this.scanner = scanner;
        this.user = user;
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n===== USER MENU (" + user.getFullName() + ") =====");
            System.out.println("1. View my profile");
            System.out.println("2. New sale           (coming soon)");
            System.out.println("3. View products      (coming soon)");
            System.out.println("4. Logout");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> viewProfile();
                case "2" -> System.out.println("New sale: not implemented yet.");
                case "3" -> System.out.println("View products: not implemented yet.");
                case "4" -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewProfile() {
        System.out.println("ID:        " + user.getId());
        System.out.println("Username:  " + user.getUsername());
        System.out.println("Full name: " + user.getFullName());
        System.out.println("Role:      " + user.getRoles());
    }
}