package com.evcharging.api.handler;

import com.evcharging.api.util.JsonUtil;
import com.evcharging.api.filter.CorsFilter;
import com.evcharging.controller.BookingController;
import com.evcharging.dto.BookingDTO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

public class BookingHandler implements HttpHandler {
    private BookingController bookingController;
    
    public BookingHandler() {
        this.bookingController = new BookingController();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        
        // Handle CORS preflight request
        try {
            if (CorsFilter.handlePreflight(exchange)) {
                return;
            }
        } catch (Exception e) {
            HttpResponseBuilder.sendErrorResponse(exchange, 500, "Error handling preflight request: " + e.getMessage());
            return;
        }
        
        try {
            // Create booking: /api/bookings
            if (path.equals("/api/bookings") && method.equals("POST")) {
                BookingDTO bookingDTO = JsonUtil.fromJson(exchange.getRequestBody(), BookingDTO.class);
                bookingController.createBooking(bookingDTO);
                HttpResponseBuilder.sendJsonResponse(exchange, 201, bookingDTO);
                return;
            }
            
            // Get booking by ID: /api/bookings/{id}
            if (path.matches("/api/bookings/\\d+") && method.equals("GET")) {
                int bookingId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                BookingDTO booking = bookingController.getBooking(bookingId);
                
                if (booking != null) {
                    HttpResponseBuilder.sendJsonResponse(exchange, 200, booking);
                } else {
                    HttpResponseBuilder.sendErrorResponse(exchange, 404, "Booking not found");
                }
                return;
            }
            
            // Cancel booking: /api/bookings/{id}/cancel
            if (path.matches("/api/bookings/\\d+/cancel") && method.equals("POST")) {
                int bookingId = Integer.parseInt(path.substring(path.lastIndexOf('/', path.lastIndexOf('/') - 1) + 1, path.lastIndexOf('/')));
                bookingController.cancelBooking(bookingId);
                HttpResponseBuilder.sendJsonResponse(exchange, 200, "Booking cancelled successfully");
                return;
            }
            
            // Complete booking: /api/bookings/{id}/complete
            if (path.matches("/api/bookings/\\d+/complete") && method.equals("POST")) {
                int bookingId = Integer.parseInt(path.substring(path.lastIndexOf('/', path.lastIndexOf('/') - 1) + 1, path.lastIndexOf('/')));
                bookingController.completeBooking(bookingId);
                HttpResponseBuilder.sendJsonResponse(exchange, 200, "Booking completed successfully");
                return;
            }
            
            // If we get here, the endpoint doesn't exist
            HttpResponseBuilder.sendErrorResponse(exchange, 404, "Endpoint not found");
            
        } catch (Exception e) {
            HttpResponseBuilder.sendErrorResponse(exchange, 500, "Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
