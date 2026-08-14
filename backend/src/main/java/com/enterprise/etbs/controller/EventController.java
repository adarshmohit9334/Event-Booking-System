package com.enterprise.etbs.controller;

import com.enterprise.etbs.entity.Event;
import com.enterprise.etbs.entity.Seat;
import com.enterprise.etbs.repository.EventRepository;
import com.enterprise.etbs.repository.SeatRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SeatRepository seatRepository;

    @PostConstruct
    public void seedDefaultEvents() {
        if (eventRepository.count() == 0) {
            List<Event> defaultEvents = List.of(
                Event.builder()
                    .id("ev-1")
                    .title("Neon Horizon: Electronic Live Music Festival")
                    .description("Experience an audiovisual journey with world-renowned electronic DJs playing under immersive neon laser setups.")
                    .date("2026-09-18")
                    .time("19:00")
                    .location("Synthetix Arena, Metro City")
                    .category("Music")
                    .image("https://images.unsplash.com/photo-1470225620780-dba8ba36b745?auto=format&fit=crop&w=800&q=80")
                    .priceVIP(999.0)
                    .priceGold(749.0)
                    .priceStandard(499.0)
                    .build(),
                Event.builder()
                    .id("ev-2")
                    .title("Stand-up Showcase: Laugh Out Loud Night")
                    .description("A stellar line-up of the country’s funniest stand-up comedians gathered for a night of observational comedy.")
                    .date("2026-08-25")
                    .time("20:30")
                    .location("The Velvet Club, Down Town")
                    .category("Comedy")
                    .image("https://images.unsplash.com/photo-1507676184212-d03ab07a01bf?auto=format&fit=crop&w=800&q=80")
                    .priceVIP(1099.0)
                    .priceGold(799.0)
                    .priceStandard(549.0)
                    .build(),
                Event.builder()
                    .id("ev-3")
                    .title("NextGen Tech Summit 2026")
                    .description("Delve into AI agents, quantum computing breakthroughs, and WebGPU graphics standards.")
                    .date("2026-10-05")
                    .time("09:00")
                    .location("Metropolitan Convention Hall")
                    .category("Tech")
                    .image("https://images.unsplash.com/photo-1540575467063-178a50c2df87?auto=format&fit=crop&w=800&q=80")
                    .priceVIP(1499.0)
                    .priceGold(999.0)
                    .priceStandard(699.0)
                    .build(),
                Event.builder()
                    .id("ev-4")
                    .title("Championship Basketball: Kings vs Titans")
                    .description("Witness the ultimate showdown of the season. High-flying dunks and buzzer-beating thrillers.")
                    .date("2026-08-30")
                    .time("18:00")
                    .location("Grand Plaza Gardens")
                    .category("Sports")
                    .image("https://images.unsplash.com/photo-1546519638-68e109498ffc?auto=format&fit=crop&w=800&q=80")
                    .priceVIP(1199.0)
                    .priceGold(849.0)
                    .priceStandard(599.0)
                    .build()
            );

            eventRepository.saveAll(defaultEvents);
            System.out.println("Default events seeded.");

            // Seed seats for each event
            Random random = new Random();
            List<String> rows = List.of("A", "B", "C", "D", "E", "F");

            for (Event event : defaultEvents) {
                List<Seat> seats = new ArrayList<>();
                for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
                    String row = rows.get(rowIndex);
                    String type = "Standard";
                    if (rowIndex < 2) type = "VIP";
                    else if (rowIndex < 4) type = "Gold";

                    for (int col = 1; col <= 10; col++) {
                        String seatNumber = row + col;
                        boolean isBooked = random.nextDouble() < 0.25;
                        seats.add(Seat.builder()
                                .id(event.getId() + "_" + seatNumber)
                                .eventId(event.getId())
                                .seatNumber(seatNumber)
                                .rowLabel(row)
                                .type(type)
                                .status(isBooked ? "booked" : "available")
                                .build());
                    }
                }
                seatRepository.saveAll(seats);
            }
            System.out.println("Default seats generated and seeded.");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Event event : events) {
            List<Seat> seats = seatRepository.findByEventId(event.getId());
            long totalSeats = seats.size();
            long bookedSeats = seats.stream().filter(s -> "booked".equals(s.getStatus())).count();
            long leftSeats = totalSeats - bookedSeats;

            Map<String, Object> eventMap = new HashMap<>();
            eventMap.put("id", event.getId());
            eventMap.put("title", event.getTitle());
            eventMap.put("description", event.getDescription());
            eventMap.put("date", event.getDate());
            eventMap.put("time", event.getTime());
            eventMap.put("location", event.getLocation());
            eventMap.put("category", event.getCategory());
            eventMap.put("image", event.getImage());

            Map<String, Double> prices = new HashMap<>();
            prices.put("VIP", event.getPriceVIP());
            prices.put("Gold", event.getPriceGold());
            prices.put("Standard", event.getPriceStandard());
            eventMap.put("prices", prices);

            eventMap.put("leftSeats", leftSeats);
            eventMap.put("totalSeats", totalSeats);

            response.add(eventMap);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/seats")
    public ResponseEntity<?> getEventSeats(@PathVariable String id) {
        List<Seat> seats = seatRepository.findByEventId(id);
        if (seats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Return Map<String, Seat>
        Map<String, Seat> seatMap = seats.stream()
                .collect(Collectors.toMap(Seat::getSeatNumber, s -> s));
        return ResponseEntity.ok(seatMap);
    }
}
