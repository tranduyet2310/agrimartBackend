package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.payload.ReviewDto;
import com.example.agriecommerce.payload.ReviewResponse;
import com.example.agriecommerce.payload.ReviewStatisticDto;
import com.example.agriecommerce.service.ReviewService;
import com.example.agriecommerce.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{userId}/{productId}")
    public ResponseEntity<ReviewDto> createReview(@PathVariable("userId") Long userId,
                                                  @PathVariable("productId") Long productId,
                                                  @RequestBody ReviewDto reviewDto) {
        ReviewDto dto = reviewService.createReview(productId, userId, reviewDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("{productId}/list")
    public ResponseEntity<ReviewResponse> getAllReviewsByProductId(
            @PathVariable("productId") Long productId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return ResponseEntity.ok(reviewService.getAllReviewsByProductId(productId, pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("{productId}/total")
    public ResponseEntity<ReviewStatisticDto> calculateTotalRating(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(reviewService.calculateTotalRating(productId));
    }

    @GetMapping("{productId}/statistic")
    public ResponseEntity<ReviewStatisticDto> statisticRating(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(reviewService.statisticRating(productId));
    }

    @GetMapping("{productId}/average")
    public ResponseEntity<ReviewStatisticDto> averageRating(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(reviewService.averageRating(productId));
    }

    @GetMapping("{supplierId}/calculate")
    public ResponseEntity<ReviewStatisticDto> supplierAverageRating(@PathVariable("supplierId") Long supplierId) {
        return ResponseEntity.ok(reviewService.supplierAverageRating(supplierId));
    }

    @GetMapping("{supplierId}/rating")
    public ResponseEntity<ResultDto> getTotalReviews(@PathVariable("supplierId") Long supplierId) {
        return ResponseEntity.ok(reviewService.getTotalReviewsBySupplier(supplierId));
    }
}
