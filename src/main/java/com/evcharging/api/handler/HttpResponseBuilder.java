package com.evcharging.api.handler;

import com.evcharging.api.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for building HTTP responses
 */
public class HttpResponseBuilder {
    
    /**
     * Sends a JSON response with the given status code and object
     *
     * @param exchange The HttpExchange to send the response to
     * @param statusCode The HTTP status code
     * @param object The object to serialize to JSON
     * @throws IOException If there's an error sending the response
     */
    public static void sendJsonResponse(HttpExchange exchange, int statusCode, Object object) throws IOException {
        // Add CORS headers
        com.evcharging.api.filter.CorsFilter.addCorsHeaders(exchange);
        
        // Convert object to JSON
        String response = JsonUtil.toJson(object);
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        
        // Set content type header
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        
        // Send the response
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
    
    /**
     * Sends an error response with the given status code and message
     *
     * @param exchange The HttpExchange to send the response to
     * @param statusCode The HTTP status code
     * @param message The error message
     * @throws IOException If there's an error sending the response
     */
    public static void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        // Add CORS headers
        com.evcharging.api.filter.CorsFilter.addCorsHeaders(exchange);
        
        // Create error object with message
        String response = JsonUtil.toJson(new ErrorResponse(message));
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        
        // Set content type header
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        
        // Send the response
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
    
    /**
     * Simple error response class for JSON serialization
     */
    private static class ErrorResponse {
        private final String error;
        
        public ErrorResponse(String error) {
            this.error = error;
        }
        
        public String getError() {
            return error;
        }
    }
}
