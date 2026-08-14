package com.enterprise.etbs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {
    @Id
    private String id;

    private String seatNumber;
    private String rowLabel;
    private String type;
    private String status;
    private String eventId;

    public Seat() {}

    public Seat(String id, String seatNumber, String rowLabel, String type, String status, String eventId) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.rowLabel = rowLabel;
        this.type = type;
        this.status = status;
        this.eventId = eventId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public String getRowLabel() { return rowLabel; }
    public void setRowLabel(String rowLabel) { this.rowLabel = rowLabel; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public static SeatBuilder builder() {
        return new SeatBuilder();
    }

    public static class SeatBuilder {
        private String id;
        private String seatNumber;
        private String rowLabel;
        private String type;
        private String status;
        private String eventId;

        public SeatBuilder id(String id) { this.id = id; return this; }
        public SeatBuilder seatNumber(String seatNumber) { this.seatNumber = seatNumber; return this; }
        public SeatBuilder rowLabel(String rowLabel) { this.rowLabel = rowLabel; return this; }
        public SeatBuilder type(String type) { this.type = type; return this; }
        public SeatBuilder status(String status) { this.status = status; return this; }
        public SeatBuilder eventId(String eventId) { this.eventId = eventId; return this; }

        public Seat build() {
            return new Seat(id, seatNumber, rowLabel, type, status, eventId);
        }
    }
}
