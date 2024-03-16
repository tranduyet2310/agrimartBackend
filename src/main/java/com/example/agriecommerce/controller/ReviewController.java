package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.ReviewDto;
import com.example.agriecommerce.service.ReviewService;
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
    public ResponseEntity<List<ReviewDto>> getAllReviewsByProductId(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(reviewService.getAllReviewsByProductId(productId));
    }

    @GetMapping("{productId}/total")
    public ResponseEntity<Long> calculateTotalRating(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(reviewService.calculateTotalRating(productId));
    }

    @GetMapping("{productId}/statistic")
    public ResponseEntity<Map<Integer, Long>> statisticRating(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(reviewService.statisticRating(productId));
    }

    @GetMapping("{productId}/average")
    public ResponseEntity<BigDecimal> averageRating(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(reviewService.averageRating(productId));
    }
}
