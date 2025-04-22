package com.evcharging.api.filter;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.util.Arrays;
import java.util.List;

public class CorsFilter {
    
    // Default values - these could be loaded from a properties file
    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
        "http://localhost:5173", 
        "http://localhost:3000", 
        "http://localhost:8080"
    );
    private static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS";
    private static final String ALLOWED_HEADERS = "Content-Type, Authorization, X-Requested-With";
    private static final String MAX_AGE = "3600";
    
    /**
     * Adds CORS headers to the response
     * @param exchange The HttpExchange to add headers to
     */
    public static void addCorsHeaders(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();
        
        // Get origin from request
        String requestOrigin = exchange.getRequestHeaders().getFirst("Origin");
        
        // Set Access-Control-Allow-Origin based on request origin
        if (requestOrigin != null && ALLOWED_ORIGINS.contains(requestOrigin)) {
            headers.set("Access-Control-Allow-Origin", requestOrigin);
        } else {
            // Default to the first allowed origin
            headers.set("Access-Control-Allow-Origin", ALLOWED_ORIGINS.get(0));
        }
        
        headers.set("Access-Control-Allow-Methods", ALLOWED_METHODS);
        headers.set("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        headers.set("Access-Control-Max-Age", MAX_AGE);
        headers.set("Access-Control-Allow-Credentials", "true");
    }
    
    /**
     * Handle CORS preflight OPTIONS request
     * @param exchange The HttpExchange to process
     * @return true if it was an OPTIONS request and was handled
     * @throws Exception If there's an error handling the request
     */
    public static boolean handlePreflight(HttpExchange exchange) throws Exception {
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            addCorsHeaders(exchange);
            exchange.sendResponseHeaders(204, -1); // No content
            exchange.close();
            return true;
        }
        return false;
    }
}
