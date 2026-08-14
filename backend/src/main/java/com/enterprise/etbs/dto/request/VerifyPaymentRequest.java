package com.enterprise.etbs.dto.request;

public class VerifyPaymentRequest {
    private String bookingId;
    private String paymentId;
    private String signature;

    public VerifyPaymentRequest() {}

    public VerifyPaymentRequest(String bookingId, String paymentId, String signature) {
        this.bookingId = bookingId;
        this.paymentId = paymentId;
        this.signature = signature;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
}
