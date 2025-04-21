package com.evcharging;

import com.evcharging.api.APIServer;
import com.evcharging.controller.UserController;
import com.evcharging.dto.UserDTO;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        try {
            // Start API Server
            System.out.println("Starting EV Charging API Server...");
            APIServer apiServer = new APIServer();
            apiServer.start();
            
            // Initialize controllers
            UserController userController = new UserController();

            // Test user operations
            UserDTO newUser = new UserDTO();
            newUser.setName("Test User");
            newUser.setEmail("test@example.com");
            newUser.setPassword("password123");
            newUser.setPhone("1234567890");
            newUser.setRole("user");
            newUser.setCreatedAt(LocalDateTime.now());

            // Create user
            userController.createUser(newUser);

            // Get all users
            System.out.println("\nAll Users:");
            userController.displayAllUsers();

            System.out.println("\nAPI server is running. Press Ctrl+C to stop.");
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}