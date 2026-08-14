package com.enterprise.etbs.dto.request;

import java.util.List;

public class BookingRequest {
    private String eventId;
    private List<String> seats;

    public BookingRequest() {}

    public BookingRequest(String eventId, List<String> seats) {
        this.eventId = eventId;
        this.seats = seats;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public List<String> getSeats() { return seats; }
    public void setSeats(List<String> seats) { this.seats = seats; }
}
