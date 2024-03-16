package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);

    Optional<List<Cart>> findByUserId(Long userId);
}
