package com.evcharging.controller;

import com.evcharging.dto.BookingDTO;
import com.evcharging.exception.BookingException;
import com.evcharging.exception.StationException;
import com.evcharging.service.BookingService;

public class BookingController {
    private BookingService bookingService;

    public BookingController() {
        this.bookingService = new BookingService();
    }

    public void createBooking(BookingDTO booking) {
        try {
            bookingService.addBooking(booking);
            System.out.println("Booking created successfully");
        } catch (BookingException | StationException e) {
            System.err.println("Error creating booking: " + e.getMessage());
        }
    }

    public BookingDTO getBooking(int bookingId) {
        try {
            return bookingService.getBookingById(bookingId);
        } catch (BookingException e) {
            System.err.println("Error getting booking: " + e.getMessage());
            return null;
        }
    }

    public void updateBooking(BookingDTO booking) {
        try {
            bookingService.updateBooking(booking);
            System.out.println("Booking updated successfully");
        } catch (BookingException e) {
            System.err.println("Error updating booking: " + e.getMessage());
        }
    }

    public void deleteBooking(int bookingId) {
        try {
            bookingService.deleteBooking(bookingId);
            System.out.println("Booking deleted successfully");
        } catch (BookingException | StationException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
        }
    }

    public void cancelBooking(int bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            System.out.println("Booking cancelled successfully");
        } catch (BookingException | StationException e) {
            System.err.println("Error cancelling booking: " + e.getMessage());
        }
    }

    public void completeBooking(int bookingId) {
        try {
            bookingService.completeBooking(bookingId);
            System.out.println("Booking completed successfully");
        } catch (BookingException e) {
            System.err.println("Error completing booking: " + e.getMessage());
        }
    }
}