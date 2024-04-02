package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Optional<List<OrderDetail>> findByOrderId(Long orderId);
}
