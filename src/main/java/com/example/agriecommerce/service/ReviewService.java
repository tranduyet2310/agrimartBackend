package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.ReviewDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReviewService {
    ReviewDto createReview(Long productId, Long userId, ReviewDto reviewDto);
    ReviewDto getReviewById(Long id);
    List<ReviewDto> getAllReviewsByProductId(Long productId);
    Long calculateTotalRating(Long productId);
    Map<Integer, Long> statisticRating(Long productId);
    BigDecimal averageRating(Long productId);
}
