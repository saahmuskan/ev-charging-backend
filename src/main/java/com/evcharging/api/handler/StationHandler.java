package com.evcharging.api.handler;

import com.evcharging.api.util.JsonUtil;
import com.evcharging.api.filter.CorsFilter;
import com.evcharging.controller.StationController;
import com.evcharging.dto.StationDTO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class StationHandler implements HttpHandler {
    private StationController stationController;
    
    public StationHandler() {
        this.stationController = new StationController();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String query = uri.getQuery();
        
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
            // Get station by ID: /api/stations/{id}
            if (path.matches("/api/stations/\\d+")) {
                int stationId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                
                if (method.equals("GET")) {
                    StationDTO station = stationController.getStation(stationId);
                    if (station != null) {
                        HttpResponseBuilder.sendJsonResponse(exchange, 200, station);
                    } else {
                        HttpResponseBuilder.sendErrorResponse(exchange, 404, "Station not found");
                    }
                } else if (method.equals("PUT")) {
                    StationDTO stationDTO = JsonUtil.fromJson(exchange.getRequestBody(), StationDTO.class);
                    stationDTO.setStationId(stationId);
                    stationController.updateStation(stationDTO);
                    HttpResponseBuilder.sendJsonResponse(exchange, 200, stationDTO);
                } else if (method.equals("DELETE")) {
                    stationController.deleteStation(stationId);
                    HttpResponseBuilder.sendJsonResponse(exchange, 200, "Station deleted");
                } else {
                    HttpResponseBuilder.sendErrorResponse(exchange, 405, "Method not allowed");
                }
                return;
            }
            
            // All stations or search by location: /api/stations
            if (path.equals("/api/stations")) {
                if (method.equals("GET")) {
                    // Check if it's a search by location
                    if (query != null && query.startsWith("location=")) {
                        String location = query.substring("location=".length());
                        List<StationDTO> stations = stationController.searchStationsByLocation(location);
                        HttpResponseBuilder.sendJsonResponse(exchange, 200, stations);
                    } else {
                        // Get all stations
                        List<StationDTO> stations = stationController.getAllStations();
                        HttpResponseBuilder.sendJsonResponse(exchange, 200, stations);
                    }
                } else if (method.equals("POST")) {
                    // Create station
                    StationDTO stationDTO = JsonUtil.fromJson(exchange.getRequestBody(), StationDTO.class);
                    stationController.addStation(stationDTO);
                    HttpResponseBuilder.sendJsonResponse(exchange, 201, stationDTO);
                } else {
                    HttpResponseBuilder.sendErrorResponse(exchange, 405, "Method not allowed");
                }
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
