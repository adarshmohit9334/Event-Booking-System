package com.enterprise.etbs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    private String id;

    private String eventId;
    private String eventTitle;
    private String eventDate;
    private String eventTime;
    private String eventLocation;
    private String eventImage;

    private String seats; // comma-separated seats (e.g., "A1, A2")
    private Double totalPrice;
    private LocalDateTime bookedAt;
    
    private String userEmail;
    private String status; // "confirmed", "cancelled", "pending"
    private String razorpayOrderId;

    public Booking() {}

    public Booking(String id, String eventId, String eventTitle, String eventDate, String eventTime, String eventLocation, String eventImage, String seats, Double totalPrice, LocalDateTime bookedAt, String userEmail, String status, String razorpayOrderId) {
        this.id = id;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventLocation = eventLocation;
        this.eventImage = eventImage;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.bookedAt = bookedAt;
        this.userEmail = userEmail;
        this.status = status;
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }

    public String getEventLocation() { return eventLocation; }
    public void setEventLocation(String eventLocation) { this.eventLocation = eventLocation; }

    public String getEventImage() { return eventImage; }
    public void setEventImage(String eventImage) { this.eventImage = eventImage; }

    public String getSeats() { return seats; }
    public void setSeats(String seats) { this.seats = seats; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRazorpayOrderId() { return razorpayOrderId; }
    public void setRazorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; }

    public static BookingBuilder builder() {
        return new BookingBuilder();
    }

    public static class BookingBuilder {
        private String id;
        private String eventId;
        private String eventTitle;
        private String eventDate;
        private String eventTime;
        private String eventLocation;
        private String eventImage;
        private String seats;
        private Double totalPrice;
        private LocalDateTime bookedAt;
        private String userEmail;
        private String status;
        private String razorpayOrderId;

        public BookingBuilder id(String id) { this.id = id; return this; }
        public BookingBuilder eventId(String eventId) { this.eventId = eventId; return this; }
        public BookingBuilder eventTitle(String eventTitle) { this.eventTitle = eventTitle; return this; }
        public BookingBuilder eventDate(String eventDate) { this.eventDate = eventDate; return this; }
        public BookingBuilder eventTime(String eventTime) { this.eventTime = eventTime; return this; }
        public BookingBuilder eventLocation(String eventLocation) { this.eventLocation = eventLocation; return this; }
        public BookingBuilder eventImage(String eventImage) { this.eventImage = eventImage; return this; }
        public BookingBuilder seats(String seats) { this.seats = seats; return this; }
        public BookingBuilder totalPrice(Double totalPrice) { this.totalPrice = totalPrice; return this; }
        public BookingBuilder bookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; return this; }
        public BookingBuilder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public BookingBuilder status(String status) { this.status = status; return this; }
        public BookingBuilder razorpayOrderId(String razorpayOrderId) { this.razorpayOrderId = razorpayOrderId; return this; }

        public Booking build() {
            return new Booking(id, eventId, eventTitle, eventDate, eventTime, eventLocation, eventImage, seats, totalPrice, bookedAt, userEmail, status, razorpayOrderId);
        }
    }
}
