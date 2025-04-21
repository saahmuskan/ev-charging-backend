package com.evcharging.api.handler;

import com.evcharging.api.util.JsonUtil;
import com.evcharging.controller.UserController;
import com.evcharging.dto.UserDTO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class LoginHandler implements HttpHandler {
    private UserController userController;
    
    public LoginHandler() {
        this.userController = new UserController();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        
        // Handle CORS preflight request
        if (method.equals("OPTIONS")) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            exchange.sendResponseHeaders(204, -1);
            return;
        }
        
        if (!method.equals("POST")) {
            HttpResponseBuilder.sendErrorResponse(exchange, 405, "Method not allowed");
            return;
        }
        
        try {
            // Login process
            Map<String, String> credentials = JsonUtil.fromJson(exchange.getRequestBody(), Map.class);
            String email = credentials.get("email");
            String password = credentials.get("password");
            
            if (email == null || password == null) {
                HttpResponseBuilder.sendErrorResponse(exchange, 400, "Email and password are required");
                return;
            }
            
            UserDTO user = userController.login(email, password);
            
            if (user != null) {
                HttpResponseBuilder.sendJsonResponse(exchange, 200, user);
            } else {
                HttpResponseBuilder.sendErrorResponse(exchange, 401, "Invalid email or password");
            }
            
        } catch (Exception e) {
            HttpResponseBuilder.sendErrorResponse(exchange, 500, "Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
