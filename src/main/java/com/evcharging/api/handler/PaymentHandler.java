package com.evcharging.api.handler;

import com.evcharging.api.util.JsonUtil;
import com.evcharging.api.filter.CorsFilter;
import com.evcharging.controller.PaymentController;
import com.evcharging.dto.PaymentDTO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class PaymentHandler implements HttpHandler {
    private PaymentController paymentController;
    
    public PaymentHandler() {
        this.paymentController = new PaymentController();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String query = uri.getQuery();
        
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
            // Process payment: /api/payments
            if (path.equals("/api/payments") && method.equals("POST")) {
                PaymentDTO paymentDTO = JsonUtil.fromJson(exchange.getRequestBody(), PaymentDTO.class);
                PaymentDTO result = paymentController.processPayment(paymentDTO);
                if (result != null) {
                    HttpResponseBuilder.sendJsonResponse(exchange, 201, result);
                } else {
                    HttpResponseBuilder.sendErrorResponse(exchange, 400, "Payment processing failed");
                }
                return;
            }
            
            // Get payment by ID: /api/payments/{id}
            if (path.matches("/api/payments/\\d+") && method.equals("GET")) {
                int paymentId = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                PaymentDTO payment = paymentController.getPaymentById(paymentId);
                
                if (payment != null) {
                    HttpResponseBuilder.sendJsonResponse(exchange, 200, payment);
                } else {
                    HttpResponseBuilder.sendErrorResponse(exchange, 404, "Payment not found");
                }
                return;
            }
            
            // Get user payments: /api/payments?userId=X
            if (path.equals("/api/payments") && query != null && query.startsWith("userId=")) {
                if (method.equals("GET")) {
                    int userId = Integer.parseInt(query.substring("userId=".length()));
                    List<PaymentDTO> payments = paymentController.getPaymentsByUserId(userId);
                    HttpResponseBuilder.sendJsonResponse(exchange, 200, payments);
                } else {
                    HttpResponseBuilder.sendErrorResponse(exchange, 405, "Method not allowed");
                }
                return;
            }
            
            // Refund payment: /api/payments/{id}/refund
            if (path.matches("/api/payments/\\d+/refund") && method.equals("POST")) {
                int paymentId = Integer.parseInt(path.substring(path.lastIndexOf('/', path.lastIndexOf('/') - 1) + 1, path.lastIndexOf('/')));
                PaymentDTO refundedPayment = paymentController.refundPayment(paymentId);
                
                if (refundedPayment != null) {
                    HttpResponseBuilder.sendJsonResponse(exchange, 200, refundedPayment);
                } else {
                    HttpResponseBuilder.sendErrorResponse(exchange, 400, "Refund failed");
                }
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
