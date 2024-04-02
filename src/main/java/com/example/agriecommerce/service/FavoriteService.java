package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.FavoriteDto;
import com.example.agriecommerce.payload.ResultDto;

import java.util.List;

public interface FavoriteService {
    FavoriteDto createFavoriteProduct(Long userId, Long productId);

    FavoriteDto getFavoriteProductById(Long id);

    List<FavoriteDto> getAllFavoriteByUserId(Long userId);

    void deleteFavoriteProductById(Long id, Long userId);

    ResultDto deleteFavoriteProduct(Long userId, Long productId);
}
