package com.enterprise.etbs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class Event {
    @Id
    private String id;

    private String title;

    @Column(length = 2000)
    private String description;

    private String date;
    private String time;
    private String location;
    private String category;
    private String image;

    private Double priceVIP;
    private Double priceGold;
    private Double priceStandard;

    public Event() {}

    public Event(String id, String title, String description, String date, String time, String location, String category, String image, Double priceVIP, Double priceGold, Double priceStandard) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.category = category;
        this.image = image;
        this.priceVIP = priceVIP;
        this.priceGold = priceGold;
        this.priceStandard = priceStandard;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Double getPriceVIP() { return priceVIP; }
    public void setPriceVIP(Double priceVIP) { this.priceVIP = priceVIP; }

    public Double getPriceGold() { return priceGold; }
    public void setPriceGold(Double priceGold) { this.priceGold = priceGold; }

    public Double getPriceStandard() { return priceStandard; }
    public void setPriceStandard(Double priceStandard) { this.priceStandard = priceStandard; }

    public static EventBuilder builder() {
        return new EventBuilder();
    }

    public static class EventBuilder {
        private String id;
        private String title;
        private String description;
        private String date;
        private String time;
        private String location;
        private String category;
        private String image;
        private Double priceVIP;
        private Double priceGold;
        private Double priceStandard;

        public EventBuilder id(String id) { this.id = id; return this; }
        public EventBuilder title(String title) { this.title = title; return this; }
        public EventBuilder description(String description) { this.description = description; return this; }
        public EventBuilder date(String date) { this.date = date; return this; }
        public EventBuilder time(String time) { this.time = time; return this; }
        public EventBuilder location(String location) { this.location = location; return this; }
        public EventBuilder category(String category) { this.category = category; return this; }
        public EventBuilder image(String image) { this.image = image; return this; }
        public EventBuilder priceVIP(Double priceVIP) { this.priceVIP = priceVIP; return this; }
        public EventBuilder priceGold(Double priceGold) { this.priceGold = priceGold; return this; }
        public EventBuilder priceStandard(Double priceStandard) { this.priceStandard = priceStandard; return this; }

        public Event build() {
            return new Event(id, title, description, date, time, location, category, image, priceVIP, priceGold, priceStandard);
        }
    }
}
