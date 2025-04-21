package com.evcharging.controller;

import java.util.List;
import com.evcharging.service.PaymentService;
import com.evcharging.dto.PaymentDTO;
import com.evcharging.exception.PaymentException;

public class PaymentController {
    
    private PaymentService paymentService;
    
    public PaymentController() {
        this.paymentService = new PaymentService();
    }
    
    public PaymentDTO processPayment(PaymentDTO payment) {
        try {
            return paymentService.processPayment(payment);
        } catch (PaymentException e) {
            System.err.println("Error processing payment: " + e.getMessage());
            return null;
        }
    }
    
    public PaymentDTO getPaymentById(int paymentId) {
        try {
            return paymentService.getPaymentById(paymentId);
        } catch (PaymentException e) {
            System.err.println("Error retrieving payment: " + e.getMessage());
            return null;
        }
    }
    
    public List<PaymentDTO> getPaymentsByUserId(int userId) {
        try {
            return paymentService.getPaymentsByUserId(userId);
        } catch (PaymentException e) {
            System.err.println("Error retrieving user payments: " + e.getMessage());
            return null;
        }
    }
    
    public PaymentDTO refundPayment(int paymentId) {
        try {
            return paymentService.refundPayment(paymentId);
        } catch (PaymentException e) {
            System.err.println("Error refunding payment: " + e.getMessage());
            return null;
        }
    }
}
