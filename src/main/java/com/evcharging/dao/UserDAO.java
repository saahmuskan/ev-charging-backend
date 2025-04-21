package com.evcharging.dao;

import com.evcharging.dto.UserDTO;
import com.evcharging.exception.UserException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    // CRUD operations
    public void addUser(UserDTO user) throws UserException {
        String sql = "INSERT INTO Users(name, email, password, phone, role) VALUES(?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getRole());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new UserException("Error adding user: " + e.getMessage());
        }
    }

    public UserDTO getUserById(int userId) throws UserException {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        UserDTO user = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = new UserDTO(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
            }
            
        } catch (SQLException e) {
            throw new UserException("Error getting user by ID: " + e.getMessage());
        }
        return user;
    }

    public List<UserDTO> getAllUsers() throws UserException {
        String sql = "SELECT * FROM Users";
        List<UserDTO> users = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                UserDTO user = new UserDTO(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                users.add(user);
            }
            
        } catch (SQLException e) {
            throw new UserException("Error getting all users: " + e.getMessage());
        }
        return users;
    }

    public void updateUser(UserDTO user) throws UserException {
        String sql = "UPDATE Users SET name = ?, email = ?, password = ?, phone = ?, role = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getRole());
            pstmt.setInt(6, user.getUserId());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new UserException("Error updating user: " + e.getMessage());
        }
    }

    public void deleteUser(int userId) throws UserException {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new UserException("Error deleting user: " + e.getMessage());
        }
    }

    public UserDTO authenticateUser(String email, String password) throws UserException {
        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";
        UserDTO user = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = new UserDTO(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
            }
            
        } catch (SQLException e) {
            throw new UserException("Error authenticating user: " + e.getMessage());
        }
        return user;
    }
}