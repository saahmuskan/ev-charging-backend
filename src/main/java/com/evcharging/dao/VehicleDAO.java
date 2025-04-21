package com.evcharging.dao;

import com.evcharging.dto.VehicleDTO;
import com.evcharging.exception.VehicleException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {
    public void addVehicle(VehicleDTO vehicle) throws VehicleException {
        String sql = "INSERT INTO Vehicles(user_id, license_plate, type, model) VALUES(?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, vehicle.getUserId());
            pstmt.setString(2, vehicle.getLicensePlate());
            pstmt.setString(3, vehicle.getType());
            pstmt.setString(4, vehicle.getModel());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vehicle.setVehicleId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new VehicleException("Error adding vehicle: " + e.getMessage());
        }
    }

    public VehicleDTO getVehicleById(int vehicleId) throws VehicleException {
        String sql = "SELECT * FROM Vehicles WHERE vehicle_id = ?";
        VehicleDTO vehicle = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                vehicle = new VehicleDTO(
                    rs.getInt("vehicle_id"),
                    rs.getInt("user_id"),
                    rs.getString("license_plate"),
                    rs.getString("type"),
                    rs.getString("model")
                );
            }
        } catch (SQLException e) {
            throw new VehicleException("Error getting vehicle by ID: " + e.getMessage());
        }
        return vehicle;
    }

    public List<VehicleDTO> getVehiclesByUserId(int userId) throws VehicleException {
        String sql = "SELECT * FROM Vehicles WHERE user_id = ?";
        List<VehicleDTO> vehicles = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                VehicleDTO vehicle = new VehicleDTO(
                    rs.getInt("vehicle_id"),
                    rs.getInt("user_id"),
                    rs.getString("license_plate"),
                    rs.getString("type"),
                    rs.getString("model")
                );
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            throw new VehicleException("Error getting vehicles by user ID: " + e.getMessage());
        }
        return vehicles;
    }

    public void updateVehicle(VehicleDTO vehicle) throws VehicleException {
        String sql = "UPDATE Vehicles SET user_id = ?, license_plate = ?, type = ?, model = ? WHERE vehicle_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, vehicle.getUserId());
            pstmt.setString(2, vehicle.getLicensePlate());
            pstmt.setString(3, vehicle.getType());
            pstmt.setString(4, vehicle.getModel());
            pstmt.setInt(5, vehicle.getVehicleId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new VehicleException("Error updating vehicle: " + e.getMessage());
        }
    }

    public void deleteVehicle(int vehicleId) throws VehicleException {
        String sql = "DELETE FROM Vehicles WHERE vehicle_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, vehicleId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new VehicleException("Error deleting vehicle: " + e.getMessage());
        }
    }
}