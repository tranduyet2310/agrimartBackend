package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.CooperativePayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CooperativePaymentRepository extends JpaRepository<CooperativePayment, Long> {
    Optional<Page<CooperativePayment>> findByUserId(Long userId, Pageable pageable);
    Optional<Page<CooperativePayment>> findBySupplierId(Long supplierId, Pageable pageable);
}
