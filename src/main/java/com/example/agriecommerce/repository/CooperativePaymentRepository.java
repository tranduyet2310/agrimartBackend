package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.CooperativePayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CooperativePaymentRepository extends JpaRepository<CooperativePayment, Long> {
    Optional<Page<CooperativePayment>> findByUserId(Long userId, Pageable pageable);
    Optional<Page<CooperativePayment>> findBySupplierId(Long supplierId, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT COALESCE(SUM(total), 0) FROM tbl_cooperative_payment c " +
            "WHERE MONTH(c.date_created) = :month AND YEAR(c.date_created) = :year " +
            "AND c.order_status IN (0, 1, 2, 3)")
    long calculateTotalRevenue(@Param("month") int month, @Param("year") int year);
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM tbl_cooperative_payment c " +
            "WHERE MONTH(c.date_created) = :month AND YEAR(c.date_created) = :year")
    long countTotalOrder(@Param("month") int month, @Param("year") int year);
}
