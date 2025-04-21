package com.evcharging.service;

import com.evcharging.dao.StationDAO;
import com.evcharging.dto.StationDTO;
import com.evcharging.exception.StationException;

import java.util.List;

public class StationService {
    private StationDAO stationDAO;

    public StationService() {
        this.stationDAO = new StationDAO();
    }

    public void addStation(StationDTO station) throws StationException {
        validateStation(station);
        stationDAO.addStation(station);
    }

    public StationDTO getStationById(int stationId) throws StationException {
        return stationDAO.getStationById(stationId);
    }

    public List<StationDTO> getAllStations() throws StationException {
        return stationDAO.getAllStations();
    }

    public void updateStation(StationDTO station) throws StationException {
        validateStation(station);
        stationDAO.updateStation(station);
    }

    public void deleteStation(int stationId) throws StationException {
        stationDAO.deleteStation(stationId);
    }

    public List<StationDTO> getStationsByLocation(String location) throws StationException {
        return stationDAO.getStationsByLocation(location);
    }

    private void validateStation(StationDTO station) throws StationException {
        if (station.getName() == null || station.getName().trim().isEmpty()) {
            throw new StationException("Station name cannot be empty");
        }
        if (station.getLocation() == null || station.getLocation().trim().isEmpty()) {
            throw new StationException("Station location cannot be empty");
        }
        if (station.getCapacity() <= 0) {
            throw new StationException("Station capacity must be positive");
        }
        if (station.getAvailableSlots() < 0 || station.getAvailableSlots() > station.getCapacity()) {
            throw new StationException("Invalid available slots value");
        }
    }
}