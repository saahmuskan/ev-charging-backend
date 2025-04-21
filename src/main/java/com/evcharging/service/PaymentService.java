package com.evcharging.service;

import java.util.List;

import com.evcharging.dao.PaymentDAO;
import com.evcharging.dto.PaymentDTO;
import com.evcharging.exception.PaymentException;

public class PaymentService {
    private PaymentDAO paymentDAO;
    
    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
    }
    
    public PaymentDTO processPayment(PaymentDTO payment) throws PaymentException {
        validatePayment(payment);
        payment.setStatus("completed");
        paymentDAO.addPayment(payment);
        return payment;
    }
    
    public PaymentDTO getPaymentById(int paymentId) throws PaymentException {
        if (paymentId <= 0) {
            throw new PaymentException("Invalid payment ID");
        }
        return paymentDAO.getPaymentById(paymentId);
    }
    
    public List<PaymentDTO> getPaymentsByUserId(int userId) throws PaymentException {
        if (userId <= 0) {
            throw new PaymentException("Invalid user ID");
        }
        return paymentDAO.getPaymentsByUserId(userId);
    }
    
    public PaymentDTO refundPayment(int paymentId) throws PaymentException {
        PaymentDTO payment = getPaymentById(paymentId);
        if (payment == null) {
            throw new PaymentException("Payment not found");
        }
        
        if (!"completed".equals(payment.getStatus())) {
            throw new PaymentException("Only completed payments can be refunded");
        }
        
        payment.setStatus("refunded");
        paymentDAO.updatePayment(payment);
        return payment;
    }
    
    private void validatePayment(PaymentDTO payment) throws PaymentException {
        if (payment == null) {
            throw new PaymentException("Payment cannot be null");
        }
        
        if (payment.getBookingId() <= 0) {
            throw new PaymentException("Invalid booking ID");
        }
        
        if (payment.getUserId() <= 0) {
            throw new PaymentException("Invalid user ID");
        }
        
        if (payment.getAmount() == null || payment.getAmount().doubleValue() <= 0) {
            throw new PaymentException("Payment amount must be positive");
        }
        
        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().trim().isEmpty()) {
            throw new PaymentException("Payment method cannot be empty");
        }
    }
}
