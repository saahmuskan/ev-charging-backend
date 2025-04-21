package com.evcharging.dto;

import java.time.LocalDateTime;

public class BookingDTO {
    private int bookingId;
    private int userId;
    private int vehicleId;
    private int stationId;
    private LocalDateTime bookingTime;
    private String status;

    // Constructors
    public BookingDTO() {}

    public BookingDTO(int bookingId, int userId, int vehicleId, int stationId, 
                    LocalDateTime bookingTime, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.stationId = stationId;
        this.bookingTime = bookingTime;
        this.status = status;
    }

    // Getters and setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public int getStationId() { return stationId; }
    public void setStationId(int stationId) { this.stationId = stationId; }
    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "BookingDTO{" +
                "bookingId=" + bookingId +
                ", userId=" + userId +
                ", vehicleId=" + vehicleId +
                ", stationId=" + stationId +
                ", bookingTime=" + bookingTime +
                ", status='" + status + '\'' +
                '}';
    }
}