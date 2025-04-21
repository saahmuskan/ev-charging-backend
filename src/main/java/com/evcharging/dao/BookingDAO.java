package com.evcharging.dao;

import com.evcharging.dto.BookingDTO;
import com.evcharging.exception.BookingException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    public void addBooking(BookingDTO booking) throws BookingException {
        String sql = "INSERT INTO Bookings(user_id, vehicle_id, station_id, status) VALUES(?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, booking.getUserId());
            pstmt.setInt(2, booking.getVehicleId());
            pstmt.setInt(3, booking.getStationId());
            pstmt.setString(4, booking.getStatus());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setBookingId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new BookingException("Error adding booking: " + e.getMessage());
        }
    }

    public BookingDTO getBookingById(int bookingId) throws BookingException {
        String sql = "SELECT * FROM Bookings WHERE booking_id = ?";
        BookingDTO booking = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                booking = new BookingDTO(
                    rs.getInt("booking_id"),
                    rs.getInt("user_id"),
                    rs.getInt("vehicle_id"),
                    rs.getInt("station_id"),
                    rs.getTimestamp("booking_time").toLocalDateTime(),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            throw new BookingException("Error getting booking by ID: " + e.getMessage());
        }
        return booking;
    }

    public List<BookingDTO> getBookingsByUserId(int userId) throws BookingException {
        String sql = "SELECT * FROM Bookings WHERE user_id = ?";
        List<BookingDTO> bookings = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                BookingDTO booking = new BookingDTO(
                    rs.getInt("booking_id"),
                    rs.getInt("user_id"),
                    rs.getInt("vehicle_id"),
                    rs.getInt("station_id"),
                    rs.getTimestamp("booking_time").toLocalDateTime(),
                    rs.getString("status")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            throw new BookingException("Error getting bookings by user ID: " + e.getMessage());
        }
        return bookings;
    }

    public void updateBooking(BookingDTO booking) throws BookingException {
        String sql = "UPDATE Bookings SET user_id = ?, vehicle_id = ?, station_id = ?, status = ? WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, booking.getUserId());
            pstmt.setInt(2, booking.getVehicleId());
            pstmt.setInt(3, booking.getStationId());
            pstmt.setString(4, booking.getStatus());
            pstmt.setInt(5, booking.getBookingId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new BookingException("Error updating booking: " + e.getMessage());
        }
    }

    public void deleteBooking(int bookingId) throws BookingException {
        String sql = "DELETE FROM Bookings WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new BookingException("Error deleting booking: " + e.getMessage());
        }
    }

    public void cancelBooking(int bookingId) throws BookingException {
        String sql = "UPDATE Bookings SET status = 'cancelled' WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new BookingException("Error cancelling booking: " + e.getMessage());
        }
    }

    public void completeBooking(int bookingId) throws BookingException {
        String sql = "UPDATE Bookings SET status = 'completed' WHERE booking_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new BookingException("Error completing booking: " + e.getMessage());
        }
    }
}