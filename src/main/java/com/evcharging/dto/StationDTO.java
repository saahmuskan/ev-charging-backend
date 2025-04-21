package com.evcharging.dto;

import java.time.LocalDateTime;

public class StationDTO {
    private int stationId;
    private String name;
    private String location;
    private int capacity;
    private int availableSlots;
    private LocalDateTime createdAt;

    // Constructors, getters, and setters
    public StationDTO() {}

    public StationDTO(int stationId, String name, String location, int capacity, int availableSlots, LocalDateTime createdAt) {
        this.stationId = stationId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.availableSlots = availableSlots;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public int getStationId() { return stationId; }
    public void setStationId(int stationId) { this.stationId = stationId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(int availableSlots) { this.availableSlots = availableSlots; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "StationDTO{" +
                "stationId=" + stationId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", capacity=" + capacity +
                ", availableSlots=" + availableSlots +
                ", createdAt=" + createdAt +
                '}';
    }
}