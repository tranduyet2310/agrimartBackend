package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.payload.ReviewDto;
import com.example.agriecommerce.payload.ReviewResponse;
import com.example.agriecommerce.payload.ReviewStatisticDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReviewService {
    ReviewDto createReview(Long productId, Long userId, ReviewDto reviewDto);
    ReviewDto getReviewById(Long id);
    ReviewResponse getAllReviewsByProductId(Long productId, int pageNo, int pageSize, String sortBy, String sortDir);
    ReviewStatisticDto calculateTotalRating(Long productId);
    ReviewStatisticDto statisticRating(Long productId);
    ReviewStatisticDto averageRating(Long productId);
    ReviewStatisticDto supplierAverageRating(Long supplierId);
    ResultDto getTotalReviewsBySupplier(Long supplierId);
}
