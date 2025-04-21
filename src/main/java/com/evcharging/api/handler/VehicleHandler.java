package com.evcharging.api.handler;

import com.evcharging.api.util.JsonUtil;
import com.evcharging.controller.VehicleController;
import com.evcharging.dto.VehicleDTO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class VehicleHandler implements HttpHandler {
    private VehicleController vehicleController;
    
    public VehicleHandler() {
        this.vehicleController = new VehicleController();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String query = uri.getQuery();
        
        // Handle CORS preflight request
        if (method.equals("OPTIONS")) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            exchange.sendResponseHeaders(204, -1);
            return;
        }
        
        try {
            // Get vehicle by ID: /api/vehicles/{id}
            if (path.matches("/api/vehicles/\\d+")) {
                int vehicleId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                
                if (method.equals("GET")) {
                    VehicleDTO vehicle = vehicleController.getVehicle(vehicleId);
                    if (vehicle != null) {
                        HttpResponseBuilder.sendJsonResponse(exchange, 200, vehicle);
                    } else {
                        HttpResponseBuilder.sendErrorResponse(exchange, 404, "Vehicle not found");
                    }
                } else if (method.equals("PUT")) {
                    VehicleDTO vehicleDTO = JsonUtil.fromJson(exchange.getRequestBody(), VehicleDTO.class);
                    vehicleDTO.setVehicleId(vehicleId);
                    vehicleController.updateVehicle(vehicleDTO);
                    HttpResponseBuilder.sendJsonResponse(exchange, 200, vehicleDTO);
                } else if (method.equals("DELETE")) {
                    vehicleController.deleteVehicle(vehicleId);
                    HttpResponseBuilder.sendJsonResponse(exchange, 200, "Vehicle deleted");
                } else {
                    HttpResponseBuilder.sendErrorResponse(exchange, 405, "Method not allowed");
                }
                return;
            }
            
            // User vehicles: /api/vehicles?userId=X
            if (path.equals("/api/vehicles") && query != null && query.startsWith("userId=")) {
                if (method.equals("GET")) {
                    int userId = Integer.parseInt(query.substring("userId=".length()));
                    List<VehicleDTO> vehicles = vehicleController.getUserVehicles(userId);
                    HttpResponseBuilder.sendJsonResponse(exchange, 200, vehicles);
                } else {
                    HttpResponseBuilder.sendErrorResponse(exchange, 405, "Method not allowed");
                }
                return;
            }
            
            // Create vehicle: /api/vehicles
            if (path.equals("/api/vehicles") && method.equals("POST")) {
                VehicleDTO vehicleDTO = JsonUtil.fromJson(exchange.getRequestBody(), VehicleDTO.class);
                vehicleController.addVehicle(vehicleDTO);
                HttpResponseBuilder.sendJsonResponse(exchange, 201, vehicleDTO);
                return;
            }
            
            // If we get here, the endpoint doesn't exist
            HttpResponseBuilder.sendErrorResponse(exchange, 404, "Endpoint not found");
            
        } catch (Exception e) {
            HttpResponseBuilder.sendErrorResponse(exchange, 500, "Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
