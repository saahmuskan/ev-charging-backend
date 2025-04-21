package com.evcharging.dao;

import com.evcharging.dto.PaymentDTO;
import com.evcharging.exception.PaymentException;

import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    
    public void addPayment(PaymentDTO payment) throws PaymentException {
        String sql = "INSERT INTO Payments(booking_id, user_id, amount, payment_method, status) VALUES(?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, payment.getBookingId());
            pstmt.setInt(2, payment.getUserId());
            pstmt.setBigDecimal(3, payment.getAmount());
            pstmt.setString(4, payment.getPaymentMethod());
            pstmt.setString(5, payment.getStatus());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    payment.setPaymentId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new PaymentException("Error adding payment: " + e.getMessage(), e);
        }
    }

    public PaymentDTO getPaymentById(int paymentId) throws PaymentException {
        String sql = "SELECT * FROM Payments WHERE payment_id = ?";
        PaymentDTO payment = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, paymentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                payment = new PaymentDTO(
                    rs.getInt("payment_id"),
                    rs.getInt("booking_id"),
                    rs.getInt("user_id"),
                    rs.getBigDecimal("amount"),
                    rs.getTimestamp("payment_time").toLocalDateTime(),
                    rs.getString("payment_method"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            throw new PaymentException("Error getting payment by ID: " + e.getMessage(), e);
        }
        return payment;
    }

    public List<PaymentDTO> getPaymentsByUserId(int userId) throws PaymentException {
        String sql = "SELECT * FROM Payments WHERE user_id = ?";
        List<PaymentDTO> payments = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                PaymentDTO payment = new PaymentDTO(
                    rs.getInt("payment_id"),
                    rs.getInt("booking_id"),
                    rs.getInt("user_id"),
                    rs.getBigDecimal("amount"),
                    rs.getTimestamp("payment_time").toLocalDateTime(),
                    rs.getString("payment_method"),
                    rs.getString("status")
                );
                payments.add(payment);
            }
        } catch (SQLException e) {
            throw new PaymentException("Error getting payments by user ID: " + e.getMessage(), e);
        }
        return payments;
    }
    
    public List<PaymentDTO> getPaymentsByBookingId(int bookingId) throws PaymentException {
        String sql = "SELECT * FROM Payments WHERE booking_id = ?";
        List<PaymentDTO> payments = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                PaymentDTO payment = new PaymentDTO(
                    rs.getInt("payment_id"),
                    rs.getInt("booking_id"),
                    rs.getInt("user_id"),
                    rs.getBigDecimal("amount"),
                    rs.getTimestamp("payment_time").toLocalDateTime(),
                    rs.getString("payment_method"),
                    rs.getString("status")
                );
                payments.add(payment);
            }
        } catch (SQLException e) {
            throw new PaymentException("Error getting payments by booking ID: " + e.getMessage(), e);
        }
        return payments;
    }

    public void updatePayment(PaymentDTO payment) throws PaymentException {
        String sql = "UPDATE Payments SET status = ? WHERE payment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, payment.getStatus());
            pstmt.setInt(2, payment.getPaymentId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new PaymentException("Error updating payment: " + e.getMessage(), e);
        }
    }
}