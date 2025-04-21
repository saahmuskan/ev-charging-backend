package com.evcharging.service;

import com.evcharging.dao.BookingDAO;
import com.evcharging.dao.StationDAO;
import com.evcharging.dto.BookingDTO;
import com.evcharging.dto.StationDTO;
import com.evcharging.exception.BookingException;
import com.evcharging.exception.StationException;

public class BookingService {
    private BookingDAO bookingDAO;
    private StationDAO stationDAO;

    public BookingService() {
        this.bookingDAO = new BookingDAO();
        this.stationDAO = new StationDAO();
    }

    public void addBooking(BookingDTO booking) throws BookingException, StationException {
        validateBooking(booking);
        
        // Check station availability
        if (!isStationAvailable(booking.getStationId())) {
            throw new BookingException("Selected station has no available slots");
        }
        
        booking.setStatus("booked");
        bookingDAO.addBooking(booking);
        
        // Update station available slots
        updateStationAvailability(booking.getStationId(), -1);
    }

    public BookingDTO getBookingById(int bookingId) throws BookingException {
        return bookingDAO.getBookingById(bookingId);
    }

    public void updateBooking(BookingDTO booking) throws BookingException {
        validateBooking(booking);
        bookingDAO.updateBooking(booking);
    }

    public void deleteBooking(int bookingId) throws BookingException, StationException {
        BookingDTO booking = bookingDAO.getBookingById(bookingId);
        if (booking != null && booking.getStatus().equals("booked")) {
            updateStationAvailability(booking.getStationId(), 1);
        }
        bookingDAO.deleteBooking(bookingId);
    }

    public void cancelBooking(int bookingId) throws BookingException, StationException {
        BookingDTO booking = bookingDAO.getBookingById(bookingId);
        if (booking != null && booking.getStatus().equals("booked")) {
            bookingDAO.cancelBooking(bookingId);
            updateStationAvailability(booking.getStationId(), 1);
        } else {
            throw new BookingException("Cannot cancel a booking that is not in 'booked' state");
        }
    }

    public void completeBooking(int bookingId) throws BookingException {
        BookingDTO booking = bookingDAO.getBookingById(bookingId);
        if (booking != null && booking.getStatus().equals("booked")) {
            bookingDAO.completeBooking(bookingId);
        } else {
            throw new BookingException("Cannot complete a booking that is not in 'booked' state");
        }
    }

    private void validateBooking(BookingDTO booking) throws BookingException {
        if (booking.getUserId() <= 0) {
            throw new BookingException("Invalid user ID");
        }
        if (booking.getVehicleId() <= 0) {
            throw new BookingException("Invalid vehicle ID");
        }
        if (booking.getStationId() <= 0) {
            throw new BookingException("Invalid station ID");
        }
    }

    private boolean isStationAvailable(int stationId) throws StationException {
        StationDTO station = stationDAO.getStationById(stationId);
        return station != null && station.getAvailableSlots() > 0;
    }

    private void updateStationAvailability(int stationId, int change) throws StationException {
        StationDTO station = stationDAO.getStationById(stationId);
        if (station != null) {
            station.setAvailableSlots(station.getAvailableSlots() + change);
            stationDAO.updateStation(station);
        }
    }
}