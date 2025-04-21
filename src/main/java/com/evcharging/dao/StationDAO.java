package com.evcharging.dao;

import com.evcharging.dto.StationDTO;
import com.evcharging.exception.StationException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StationDAO {
    public void addStation(StationDTO station) throws StationException {
        String sql = "INSERT INTO Stations(name, location, capacity, available_slots) VALUES(?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, station.getName());
            pstmt.setString(2, station.getLocation());
            pstmt.setInt(3, station.getCapacity());
            pstmt.setInt(4, station.getAvailableSlots());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    station.setStationId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new StationException("Error adding station: " + e.getMessage());
        }
    }

    public StationDTO getStationById(int stationId) throws StationException {
        String sql = "SELECT * FROM Stations WHERE station_id = ?";
        StationDTO station = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, stationId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                station = new StationDTO(
                    rs.getInt("station_id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getInt("capacity"),
                    rs.getInt("available_slots"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            throw new StationException("Error getting station by ID: " + e.getMessage());
        }
        return station;
    }

    public List<StationDTO> getAllStations() throws StationException {
        String sql = "SELECT * FROM Stations";
        List<StationDTO> stations = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                StationDTO station = new StationDTO(
                    rs.getInt("station_id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getInt("capacity"),
                    rs.getInt("available_slots"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                stations.add(station);
            }
        } catch (SQLException e) {
            throw new StationException("Error getting all stations: " + e.getMessage());
        }
        return stations;
    }

    public void updateStation(StationDTO station) throws StationException {
        String sql = "UPDATE Stations SET name = ?, location = ?, capacity = ?, available_slots = ? WHERE station_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, station.getName());
            pstmt.setString(2, station.getLocation());
            pstmt.setInt(3, station.getCapacity());
            pstmt.setInt(4, station.getAvailableSlots());
            pstmt.setInt(5, station.getStationId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new StationException("Error updating station: " + e.getMessage());
        }
    }

    public void deleteStation(int stationId) throws StationException {
        String sql = "DELETE FROM Stations WHERE station_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, stationId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new StationException("Error deleting station: " + e.getMessage());
        }
    }

    public List<StationDTO> getStationsByLocation(String location) throws StationException {
        String sql = "SELECT * FROM Stations WHERE location LIKE ?";
        List<StationDTO> stations = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + location + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                StationDTO station = new StationDTO(
                    rs.getInt("station_id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    rs.getInt("capacity"),
                    rs.getInt("available_slots"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                stations.add(station);
            }
        } catch (SQLException e) {
            throw new StationException("Error getting stations by location: " + e.getMessage());
        }
        return stations;
    }
}