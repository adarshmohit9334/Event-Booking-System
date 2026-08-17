package com.enterprise.etbs.repository;

import com.enterprise.etbs.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, String> {
    List<Event> findByOrganizerEmail(String organizerEmail);
}
