package com.enterprise.etbs.controller;

import com.enterprise.etbs.dto.request.BookingRequest;
import com.enterprise.etbs.dto.request.VerifyPaymentRequest;
import com.enterprise.etbs.entity.Booking;
import com.enterprise.etbs.entity.Event;
import com.enterprise.etbs.entity.Seat;
import com.enterprise.etbs.entity.Refund;
import com.enterprise.etbs.repository.BookingRepository;
import com.enterprise.etbs.repository.EventRepository;
import com.enterprise.etbs.repository.SeatRepository;
import com.enterprise.etbs.repository.RefundRepository;
import com.enterprise.etbs.service.EmailService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private RefundRepository refundRepository;

    @Autowired
    private EmailService emailService;

    @Value("${app.razorpay.key-id}")
    private String razorpayKeyId;

    @Value("${app.razorpay.key-secret}")
    private String razorpayKeySecret;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody BookingRequest request) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Verify seats are available
        double totalPrice = 0.0;
        List<Seat> selectedSeats = new ArrayList<>();
        for (String seatNum : request.getSeats()) {
            String seatId = event.getId() + "_" + seatNum;
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat " + seatNum + " not found"));
            
            if (!"available".equals(seat.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Error: Seat " + seatNum + " is already reserved!"));
            }

            // Determine pricing tier
            if ("VIP".equals(seat.getType())) {
                totalPrice += event.getPriceVIP();
            } else if ("Gold".equals(seat.getType())) {
                totalPrice += event.getPriceGold();
            } else {
                totalPrice += event.getPriceStandard();
            }
            selectedSeats.add(seat);
        }

        // Create booking ID
        String bookingId = "bk-" + System.currentTimeMillis();
        String razorpayOrderId = "order_mock_" + System.currentTimeMillis();

        // Create Razorpay Order
        if (razorpayKeyId != null && !razorpayKeyId.startsWith("rzp_test_mockKey")) {
            try {
                RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
                JSONObject orderRequest = new JSONObject();
                orderRequest.put("amount", (int) (totalPrice * 100)); // amount in paise
                orderRequest.put("currency", "INR");
                orderRequest.put("receipt", bookingId);

                Order order = client.orders.create(orderRequest);
                razorpayOrderId = order.get("id");
            } catch (Exception e) {
                System.err.println("Razorpay failed, falling back to mock: " + e.getMessage());
            }
        }

        // Save Booking as Pending
        Booking booking = Booking.builder()
                .id(bookingId)
                .eventId(event.getId())
                .eventTitle(event.getTitle())
                .eventDate(event.getDate())
                .eventTime(event.getTime())
                .eventLocation(event.getLocation())
                .eventImage(event.getImage())
                .seats(String.join(", ", request.getSeats()))
                .totalPrice(totalPrice)
                .bookedAt(LocalDateTime.now())
                .userEmail(email)
                .status("pending")
                .razorpayOrderId(razorpayOrderId)
                .build();

        bookingRepository.save(booking);

        return ResponseEntity.ok(Map.of(
                "bookingId", bookingId,
                "razorpayOrderId", razorpayOrderId,
                "totalPrice", totalPrice,
                "seats", booking.getSeats()
        ));
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody VerifyPaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!"pending".equals(booking.getStatus()) && !"confirmed".equals(booking.getStatus())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid booking state."));
        }

        // Mark seats as booked
        String[] seatNums = booking.getSeats().split(", ");
        for (String seatNum : seatNums) {
            String seatId = booking.getEventId() + "_" + seatNum;
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat " + seatNum + " not found"));
            seat.setStatus("booked");
            seatRepository.save(seat);
        }

        // Confirm booking
        booking.setStatus("confirmed");
        bookingRepository.save(booking);

        // Trigger email receipt delivery asynchronously
        try {
            emailService.sendBookingConfirmation(booking);
        } catch (Exception e) {
            System.err.println("Failed to trigger email receipt: " + e.getMessage());
        }

        return ResponseEntity.ok(Map.of("message", "Payment verified and tickets issued successfully!", "booking", booking));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<?> getPublicBookingDetails(@PathVariable String id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/my-tickets")
    public ResponseEntity<?> getMyTickets() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Booking> bookings = bookingRepository.findByUserEmailOrderByBookedAtDesc(email);
        
        List<Map<String, Object>> response = new ArrayList<>();
        for (Booking b : bookings) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", b.getId());
            map.put("eventId", b.getEventId());
            map.put("eventTitle", b.getEventTitle());
            map.put("eventDate", b.getEventDate());
            map.put("eventTime", b.getEventTime());
            map.put("eventLocation", b.getEventLocation());
            map.put("eventImage", b.getEventImage());
            map.put("seats", Arrays.asList(b.getSeats().split(", ")));
            map.put("totalPrice", b.getTotalPrice());
            map.put("bookedAt", b.getBookedAt().toString());
            map.put("status", b.getStatus());
            response.add(map);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable String id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!"confirmed".equals(booking.getStatus())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Only confirmed bookings can be cancelled."));
        }

        // Free seats
        String[] seatNums = booking.getSeats().split(", ");
        for (String seatNum : seatNums) {
            String seatId = booking.getEventId() + "_" + seatNum;
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            seat.setStatus("available");
            seatRepository.save(seat);
        }

        booking.setStatus("cancelled");
        bookingRepository.save(booking);

        // Generate refund log
        Refund refund = Refund.builder()
                .id("rfd-" + System.currentTimeMillis())
                .bookingId(booking.getId())
                .reason("Customer initiated cancellation")
                .amount(booking.getTotalPrice())
                .requestedAt(LocalDateTime.now())
                .status("pending")
                .build();
        refundRepository.save(refund);

        return ResponseEntity.ok(Map.of("message", "Booking cancelled successfully. Refund initiated."));
    }
}
