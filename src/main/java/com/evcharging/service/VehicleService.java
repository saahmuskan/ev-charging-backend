package com.evcharging.service;

import com.evcharging.dao.VehicleDAO;
import com.evcharging.dto.VehicleDTO;
import com.evcharging.exception.VehicleException;

import java.util.List;

public class VehicleService {
    private VehicleDAO vehicleDAO;

    public VehicleService() {
        this.vehicleDAO = new VehicleDAO();
    }

    public void addVehicle(VehicleDTO vehicle) throws VehicleException {
        validateVehicle(vehicle);
        vehicleDAO.addVehicle(vehicle);
    }

    public VehicleDTO getVehicleById(int vehicleId) throws VehicleException {
        return vehicleDAO.getVehicleById(vehicleId);
    }

    public List<VehicleDTO> getVehiclesByUserId(int userId) throws VehicleException {
        return vehicleDAO.getVehiclesByUserId(userId);
    }

    public void updateVehicle(VehicleDTO vehicle) throws VehicleException {
        validateVehicle(vehicle);
        vehicleDAO.updateVehicle(vehicle);
    }

    public void deleteVehicle(int vehicleId) throws VehicleException {
        vehicleDAO.deleteVehicle(vehicleId);
    }

    private void validateVehicle(VehicleDTO vehicle) throws VehicleException {
        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().trim().isEmpty()) {
            throw new VehicleException("License plate cannot be empty");
        }
        if (vehicle.getType() == null || vehicle.getType().trim().isEmpty()) {
            throw new VehicleException("Vehicle type cannot be empty");
        }
    }
}