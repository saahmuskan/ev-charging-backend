package com.evcharging.dto;

public class VehicleDTO {
    private int vehicleId;
    private int userId;
    private String licensePlate;
    private String type;
    private String model;

    // Constructors
    public VehicleDTO() {}

    public VehicleDTO(int vehicleId, int userId, String licensePlate, String type, String model) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.licensePlate = licensePlate;
        this.type = type;
        this.model = model;
    }

    // Getters and Setters
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    @Override
    public String toString() {
        return "VehicleDTO{" +
                "vehicleId=" + vehicleId +
                ", userId=" + userId +
                ", licensePlate='" + licensePlate + '\'' +
                ", type='" + type + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}