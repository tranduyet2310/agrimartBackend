package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.CartDto;

import java.util.List;

public interface CartService {
    CartDto addToCart(Long userId, Long productId);
    CartDto changeQuantity(Long userId, Long productId, Integer quantity);
    void removeFromCart(Long id);
    List<CartDto> getAllItemsByUserId(Long userId);
}
