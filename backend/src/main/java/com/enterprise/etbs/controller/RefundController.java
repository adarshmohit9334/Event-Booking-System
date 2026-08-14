package com.enterprise.etbs.controller;

import com.enterprise.etbs.entity.Refund;
import com.enterprise.etbs.repository.RefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/refunds")
@CrossOrigin(origins = "*")
public class RefundController {

    @Autowired
    private RefundRepository refundRepository;

    @GetMapping("/admin/queue")
    public ResponseEntity<?> getRefundQueue() {
        List<Refund> refunds = refundRepository.findAll();
        // Sort by requestedAt descending
        refunds.sort((r1, r2) -> r2.getRequestedAt().compareTo(r1.getRequestedAt()));
        return ResponseEntity.ok(refunds);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<?> processRefund(@PathVariable String id, @RequestBody Map<String, String> request) {
        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refund request not found"));

        String status = request.get("status");
        if (!"approved".equals(status) && !"rejected".equals(status)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid status value. Must be 'approved' or 'rejected'"));
        }

        refund.setStatus(status);
        refundRepository.save(refund);

        return ResponseEntity.ok(Map.of("message", "Refund status updated to: " + status, "refund", refund));
    }
}
