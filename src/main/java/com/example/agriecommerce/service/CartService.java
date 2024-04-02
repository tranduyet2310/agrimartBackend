package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.CartDto;
import com.example.agriecommerce.payload.ResultDto;

import java.util.List;

public interface CartService {
    CartDto addToCart(Long userId, Long productId);
    CartDto changeQuantity(Long userId, Long productId, Integer quantity);
    ResultDto removeFromCart(Long userId, Long productId);
    List<CartDto> getAllItemsByUserId(Long userId);
    ResultDto deleteAllItemsByUserId(Long userId);
}
