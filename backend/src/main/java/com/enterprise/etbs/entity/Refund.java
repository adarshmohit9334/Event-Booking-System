package com.enterprise.etbs.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "refunds")
public class Refund {

    @Id
    private String id;
    private String bookingId;
    private String reason;
    private Double amount;
    private LocalDateTime requestedAt;
    private String status; // pending, approved, rejected

    public Refund() {}

    public Refund(String id, String bookingId, String reason, Double amount, LocalDateTime requestedAt, String status) {
        this.id = id;
        this.bookingId = bookingId;
        this.reason = reason;
        this.amount = amount;
        this.requestedAt = requestedAt;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String bookingId;
        private String reason;
        private Double amount;
        private LocalDateTime requestedAt;
        private String status;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder bookingId(String bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder amount(Double amount) {
            this.amount = amount;
            return this;
        }

        public Builder requestedAt(LocalDateTime requestedAt) {
            this.requestedAt = requestedAt;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Refund build() {
            return new Refund(id, bookingId, reason, amount, requestedAt, status);
        }
    }
}
