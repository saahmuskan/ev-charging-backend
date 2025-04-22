package com.evcharging;

import com.evcharging.api.APIServer;
import com.evcharging.controller.UserController;
import com.evcharging.dao.DBConnection;
import com.evcharging.dto.UserDTO;
import com.evcharging.exception.UserException;

import java.time.LocalDateTime;

public class Main {
    private static APIServer apiServer;

    public static void main(String[] args) {
        try {
            // Initialize the database first
            System.out.println("Setting up database...");
            DBConnection.setupDatabase();
            
            // Start API Server
            System.out.println("Starting EV Charging API Server...");
            apiServer = new APIServer();
            apiServer.start();
            
            // Add a shutdown hook to gracefully stop the server
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down server...");
                if (apiServer != null) {
                    apiServer.stop();
                }
                DBConnection.closeConnection();
                System.out.println("Server stopped and database connection closed.");
            }));
            
            // Initialize controllers
            UserController userController = new UserController();

            // Check if test user exists, create if not
            try {
                UserDTO existingUser = userController.login("test@example.com", "password123");
                if (existingUser == null) {
                    createTestUser(userController);
                } else {
                    System.out.println("\nTest user already exists.");
                }
            } catch (Exception e) {
                // User doesn't exist or there was an error, create the user
                createTestUser(userController);
            }

            // Get all users
            System.out.println("\nAll Users:");
            userController.displayAllUsers();

            System.out.println("\nAPI server is running. Press Ctrl+C to stop.");
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
            
            // Ensure we clean up resources on failure
            if (apiServer != null) {
                apiServer.stop();
            }
            DBConnection.closeConnection();
            
            // Exit with error code
            System.exit(1);
        }
    }
    
    private static void createTestUser(UserController userController) {
        System.out.println("Creating test user...");
        UserDTO newUser = new UserDTO();
        newUser.setName("Test User");
        newUser.setEmail("test@example.com");
        newUser.setPassword("password123");
        newUser.setPhone("1234567890");
        newUser.setRole("user");
        newUser.setCreatedAt(LocalDateTime.now());
        userController.createUser(newUser);
        System.out.println("Test user created successfully.");
    }
}