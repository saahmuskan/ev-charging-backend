package com.evcharging.service;

import com.evcharging.dao.UserDAO;
import com.evcharging.dto.UserDTO;
import com.evcharging.exception.UserException;

import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public void addUser(UserDTO user) throws UserException {
        // Validate user data before adding
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new UserException("Name cannot be empty");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new UserException("Invalid email format");
        }
        userDAO.addUser(user);
    }

    public UserDTO getUserById(int userId) throws UserException {
        return userDAO.getUserById(userId);
    }

    public List<UserDTO> getAllUsers() throws UserException {
        return userDAO.getAllUsers();
    }

    public void updateUser(UserDTO user) throws UserException {
        // Validate user data before updating
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new UserException("Name cannot be empty");
        }
        userDAO.updateUser(user);
    }

    public void deleteUser(int userId) throws UserException {
        userDAO.deleteUser(userId);
    }

    public UserDTO authenticateUser(String email, String password) throws UserException {
        if (email == null || password == null) {
            throw new UserException("Email and password are required");
        }
        return userDAO.authenticateUser(email, password);
    }
}