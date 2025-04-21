package com.evcharging.controller;

import com.evcharging.dto.UserDTO;
import com.evcharging.exception.UserException;
import com.evcharging.service.UserService;

import java.util.List;

public class UserController {
    private UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    public void createUser(UserDTO user) {
        try {
            userService.addUser(user);
            System.out.println("User created successfully");
        } catch (UserException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    public UserDTO getUser(int userId) {
        try {
            return userService.getUserById(userId);
        } catch (UserException e) {
            System.err.println("Error getting user: " + e.getMessage());
            return null;
        }
    }

    public void displayAllUsers() {
        try {
            userService.getAllUsers().forEach(System.out::println);
        } catch (UserException e) {
            System.err.println("Error getting users: " + e.getMessage());
        }
    }

    public void updateUser(UserDTO user) {
        try {
            userService.updateUser(user);
            System.out.println("User updated successfully");
        } catch (UserException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
    }

    public void deleteUser(int userId) {
        try {
            userService.deleteUser(userId);
            System.out.println("User deleted successfully");
        } catch (UserException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    public UserDTO login(String email, String password) {
        try {
            UserDTO user = userService.authenticateUser(email, password);
            if (user != null) {
                System.out.println("Login successful. Welcome, " + user.getName());
            } else {
                System.out.println("Invalid email or password");
            }
            return user;
        } catch (UserException e) {
            System.err.println("Login error: " + e.getMessage());
            return null;
        }
    }

    public List<UserDTO> getAllUsers() {
        try {
            return userService.getAllUsers();
        } catch (UserException e) {
            System.err.println("Error getting users: " + e.getMessage());
            return null;
        }
    }
}