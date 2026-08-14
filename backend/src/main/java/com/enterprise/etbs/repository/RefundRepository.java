package com.enterprise.etbs.repository;

import com.enterprise.etbs.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, String> {
    List<Refund> findByStatus(String status);
}
