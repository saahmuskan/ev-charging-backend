package com.evcharging.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String DB_NAME = "ev_charging";
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String JDBC_URL = SERVER_URL + DB_NAME;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Root@123";
    private static Connection connection = null;

    private DBConnection() {}

    /**
     * Ensures the database exists before attempting to connect to it
     */
    private static void ensureDatabaseExists() throws SQLException {
        try (Connection serverConnection = DriverManager.getConnection(SERVER_URL, USERNAME, PASSWORD)) {
            // Check if database exists
            ResultSet resultSet = serverConnection.getMetaData().getCatalogs();
            boolean databaseExists = false;
            
            while (resultSet.next()) {
                String databaseName = resultSet.getString(1);
                if (databaseName.equals(DB_NAME)) {
                    databaseExists = true;
                    break;
                }
            }
            
            // Create database if it doesn't exist
            if (!databaseExists) {
                try (Statement stmt = serverConnection.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE " + DB_NAME);
                    System.out.println("Database '" + DB_NAME + "' created successfully");
                }
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                ensureDatabaseExists();
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                System.err.println("Error connecting to database: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
    
    public static void setupDatabase() {
        try (Connection conn = getConnection()) {
            System.out.println("Connected to the database");
            
            // Create Users table
            String createUserTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "user_id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "name TEXT NOT NULL," +
                "email VARCHAR(255) UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "phone VARCHAR(20)," +
                "role VARCHAR(20) DEFAULT 'user'," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            
            // Create Stations table
            String createStationTable = "CREATE TABLE IF NOT EXISTS Stations (" +
                "station_id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(255) NOT NULL," +
                "location VARCHAR(255) NOT NULL," +
                "capacity INTEGER NOT NULL," +
                "available_slots INTEGER NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            
            // Create Vehicles table
            String createVehicleTable = "CREATE TABLE IF NOT EXISTS Vehicles (" +
                "vehicle_id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "user_id INTEGER NOT NULL," +
                "license_plate VARCHAR(20) NOT NULL," +
                "type VARCHAR(50) NOT NULL," +
                "model VARCHAR(50)," +
                "FOREIGN KEY (user_id) REFERENCES Users(user_id))";
            
            // Create Bookings table
            String createBookingTable = "CREATE TABLE IF NOT EXISTS Bookings (" +
                "booking_id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "user_id INTEGER NOT NULL," +
                "vehicle_id INTEGER NOT NULL," +
                "station_id INTEGER NOT NULL," +
                "booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "status VARCHAR(20) NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES Users(user_id)," +
                "FOREIGN KEY (vehicle_id) REFERENCES Vehicles(vehicle_id)," +
                "FOREIGN KEY (station_id) REFERENCES Stations(station_id))";
            
            // Create Payments table
            String createPaymentTable = "CREATE TABLE IF NOT EXISTS Payments (" +
                "payment_id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "booking_id INTEGER NOT NULL," +
                "user_id INTEGER NOT NULL," +
                "amount DECIMAL(10,2) NOT NULL," +
                "payment_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "payment_method VARCHAR(50) NOT NULL," +
                "status VARCHAR(20) NOT NULL," +
                "FOREIGN KEY (booking_id) REFERENCES Bookings(booking_id)," +
                "FOREIGN KEY (user_id) REFERENCES Users(user_id))";
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createUserTable);
                stmt.execute(createStationTable);
                stmt.execute(createVehicleTable);
                stmt.execute(createBookingTable);
                stmt.execute(createPaymentTable);
                
                System.out.println("Database tables created successfully");
            }
        } catch (SQLException e) {
            System.err.println("Database setup error: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        setupDatabase();
        closeConnection();
    }
}