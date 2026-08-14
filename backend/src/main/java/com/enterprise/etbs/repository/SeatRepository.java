package com.enterprise.etbs.repository;

import com.enterprise.etbs.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, String> {
    List<Seat> findByEventId(String eventId);
}
