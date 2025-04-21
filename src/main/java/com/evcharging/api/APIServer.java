package com.evcharging.api;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.evcharging.api.handler.*;

public class APIServer {
    private HttpServer server;
    private static final int PORT = 8080;
    
    public APIServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Create context for each endpoint
        // Users endpoints
        server.createContext("/api/users", new UserHandler());
        server.createContext("/api/users/login", new LoginHandler());
        
        // Stations endpoints
        server.createContext("/api/stations", new StationHandler());
        
        // Vehicles endpoints
        server.createContext("/api/vehicles", new VehicleHandler());
        
        // Bookings endpoints
        server.createContext("/api/bookings", new BookingHandler());
        
        // Payments endpoints
        server.createContext("/api/payments", new PaymentHandler());
        
        // Set executor
        server.setExecutor(Executors.newCachedThreadPool());
    }
    
    public void start() {
        server.start();
        System.out.println("Server started on port " + PORT);
        System.out.println("Access the API at http://localhost:" + PORT + "/api/");
    }
    
    public void stop() {
        server.stop(0);
        System.out.println("Server stopped");
    }
}
