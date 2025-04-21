package com.evcharging.controller;

import com.evcharging.dto.VehicleDTO;
import com.evcharging.exception.VehicleException;
import com.evcharging.service.VehicleService;

import java.util.List;

public class VehicleController {
    private VehicleService vehicleService;

    public VehicleController() {
        this.vehicleService = new VehicleService();
    }

    public void addVehicle(VehicleDTO vehicle) {
        try {
            vehicleService.addVehicle(vehicle);
            System.out.println("Vehicle added successfully");
        } catch (VehicleException e) {
            System.err.println("Error adding vehicle: " + e.getMessage());
        }
    }

    public VehicleDTO getVehicle(int vehicleId) {
        try {
            return vehicleService.getVehicleById(vehicleId);
        } catch (VehicleException e) {
            System.err.println("Error getting vehicle: " + e.getMessage());
            return null;
        }
    }

    public List<VehicleDTO> getUserVehicles(int userId) {
        try {
            return vehicleService.getVehiclesByUserId(userId);
        } catch (VehicleException e) {
            System.err.println("Error getting user vehicles: " + e.getMessage());
            return null;
        }
    }

    public void updateVehicle(VehicleDTO vehicle) {
        try {
            vehicleService.updateVehicle(vehicle);
            System.out.println("Vehicle updated successfully");
        } catch (VehicleException e) {
            System.err.println("Error updating vehicle: " + e.getMessage());
        }
    }

    public void deleteVehicle(int vehicleId) {
        try {
            vehicleService.deleteVehicle(vehicleId);
            System.out.println("Vehicle deleted successfully");
        } catch (VehicleException e) {
            System.err.println("Error deleting vehicle: " + e.getMessage());
        }
    }
}