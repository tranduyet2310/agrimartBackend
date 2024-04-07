package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Page<Order>> findByUserId(Long userId, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT COUNT(o.id) FROM tbl_order o " +
            "LEFT JOIN tbl_order_detail od ON o.id = od.order_id " +
            "WHERE o.user_id = :userId AND od.product_id = :productId AND o.order_status = 3")
    Integer hasUserPurchasedProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}
