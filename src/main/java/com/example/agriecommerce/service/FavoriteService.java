package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.FavoriteDto;

import java.util.List;

public interface FavoriteService {
    FavoriteDto createFavoriteProduct(Long userId, Long productId);

    FavoriteDto getFavoriteProductById(Long id);

    List<FavoriteDto> getAllFavoriteByUserId(Long userId);

    void deleteFavoriteProductById(Long id, Long userId);

    void deleteFavoriteProduct(Long userId, Long productId);
}
