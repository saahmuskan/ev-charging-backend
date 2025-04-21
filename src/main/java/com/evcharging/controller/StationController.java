package com.evcharging.controller;

import com.evcharging.dto.StationDTO;
import com.evcharging.exception.StationException;
import com.evcharging.service.StationService;

import java.util.List;

public class StationController {
    private StationService stationService;

    public StationController() {
        this.stationService = new StationService();
    }

    public void addStation(StationDTO station) {
        try {
            stationService.addStation(station);
            System.out.println("Station added successfully");
        } catch (StationException e) {
            System.err.println("Error adding station: " + e.getMessage());
        }
    }

    public StationDTO getStation(int stationId) {
        try {
            return stationService.getStationById(stationId);
        } catch (StationException e) {
            System.err.println("Error getting station: " + e.getMessage());
            return null;
        }
    }

    public void displayAllStations() {
        try {
            stationService.getAllStations().forEach(System.out::println);
        } catch (StationException e) {
            System.err.println("Error getting stations: " + e.getMessage());
        }
    }

    public void updateStation(StationDTO station) {
        try {
            stationService.updateStation(station);
            System.out.println("Station updated successfully");
        } catch (StationException e) {
            System.err.println("Error updating station: " + e.getMessage());
        }
    }

    public void deleteStation(int stationId) {
        try {
            stationService.deleteStation(stationId);
            System.out.println("Station deleted successfully");
        } catch (StationException e) {
            System.err.println("Error deleting station: " + e.getMessage());
        }
    }

    public List<StationDTO> searchStationsByLocation(String location) {
        try {
            return stationService.getStationsByLocation(location);
        } catch (StationException e) {
            System.err.println("Error searching stations: " + e.getMessage());
            return null;
        }
    }

    public List<StationDTO> getAllStations() {
        try {
            return stationService.getAllStations();
        } catch (StationException e) {
            System.err.println("Error getting stations: " + e.getMessage());
            return null;
        }
    }
}