package com.evcharging.api.handler;

import com.evcharging.api.util.JsonUtil;
import com.evcharging.api.filter.CorsFilter;
import com.evcharging.controller.UserController;
import com.evcharging.dto.UserDTO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class UserHandler implements HttpHandler {
    private UserController userController;
    
    public UserHandler() {
        this.userController = new UserController();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        
        // Handle CORS preflight request
        try {
            if (CorsFilter.handlePreflight(exchange)) {
                return;
            }
        } catch (Exception e) {
            HttpResponseBuilder.sendErrorResponse(exchange, 500, "Error handling preflight request: " + e.getMessage());
            return;
        }
        
        try {
            // Get user by ID: /api/users/{id}
            if (path.matches("/api/users/\\d+")) {
                if (method.equals("GET")) {
                    int userId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                    UserDTO user = userController.getUser(userId);
                    
                    if (user != null) {
                        HttpResponseBuilder.sendJsonResponse(exchange, 200, user);
                    } else {
                        HttpResponseBuilder.sendErrorResponse(exchange, 404, "User not found");
                    }
                    return;
                } else if (method.equals("DELETE")) {
                    int userId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                    userController.deleteUser(userId);
                    HttpResponseBuilder.sendJsonResponse(exchange, 200, "User deleted");
                    return;
                } else if (method.equals("PUT")) {
                    // Update user
                    int userId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                    
                    try (InputStream is = exchange.getRequestBody()) {
                        UserDTO userDTO = JsonUtil.fromJson(is, UserDTO.class);
                        userDTO.setUserId(userId);
                        userController.updateUser(userDTO);
                        HttpResponseBuilder.sendJsonResponse(exchange, 200, userDTO);
                    }
                    return;
                }
            }
            
            // All users: /api/users
            if (path.equals("/api/users")) {
                if (method.equals("GET")) {
                    try {
                        HttpResponseBuilder.sendJsonResponse(exchange, 200, userController.getAllUsers());
                    } catch (Exception e) {
                        HttpResponseBuilder.sendErrorResponse(exchange, 500, "Error getting users: " + e.getMessage());
                    }
                    return;
                } else if (method.equals("POST")) {
                    // Create user
                    try (InputStream is = exchange.getRequestBody()) {
                        UserDTO userDTO = JsonUtil.fromJson(is, UserDTO.class);
                        userController.createUser(userDTO);
                        HttpResponseBuilder.sendJsonResponse(exchange, 201, userDTO);
                    }
                    return;
                }
            }
            
            // If we get here, the endpoint doesn't exist
            HttpResponseBuilder.sendErrorResponse(exchange, 404, "Endpoint not found");
            
        } catch (Exception e) {
            HttpResponseBuilder.sendErrorResponse(exchange, 500, "Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
