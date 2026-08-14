package com.enterprise.etbs.repository;

import com.enterprise.etbs.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> {
}
