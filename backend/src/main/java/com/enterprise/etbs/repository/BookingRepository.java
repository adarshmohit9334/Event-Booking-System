package com.enterprise.etbs.repository;

import com.enterprise.etbs.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByUserEmailOrderByBookedAtDesc(String userEmail);
}
