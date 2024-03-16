package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Product;
import com.example.agriecommerce.entity.Review;
import com.example.agriecommerce.entity.User;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.ReviewDto;
import com.example.agriecommerce.repository.ProductRepository;
import com.example.agriecommerce.repository.ReviewRepository;
import com.example.agriecommerce.repository.UserRepository;
import com.example.agriecommerce.service.ReviewService;
import com.example.agriecommerce.utils.DateTimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             ProductRepository productRepository,
                             ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ReviewDto createReview(Long productId, Long userId, ReviewDto reviewDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );

        LocalDate date = DateTimeUtil.convertToLocalDate(reviewDto.getReviewDate());

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setFeedBack(reviewDto.getFeedBack());
        review.setRating(reviewDto.getRating());
        review.setReviewDate(date);

        Review savedReview = reviewRepository.save(review);

        return modelMapper.map(savedReview, ReviewDto.class);
    }

    @Override
    public ReviewDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("review", "id", id)
        );
        return modelMapper.map(review, ReviewDto.class);
    }

    @Override
    public List<ReviewDto> getAllReviewsByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId).orElseThrow(
                () -> new ResourceNotFoundException("product does not have any review " + productId)
        );
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Long calculateTotalRating(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );
        return reviewRepository.countByProductId(productId);
    }

    @Override
    public Map<Integer, Long> statisticRating(Long productId) {
        Map<Integer, Long> ratingMap = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            Long count = reviewRepository.countByRating(productId, i);
            if (ObjectUtils.isEmpty(count)) count = 0L;
            ratingMap.put(i, count);
        }
        return ratingMap;
    }

    @Override
    public BigDecimal averageRating(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );

        Long total = reviewRepository.countByProductId(productId);
        BigDecimal totalRating = BigDecimal.valueOf(total);
        BigDecimal values = reviewRepository.getTotalRating(productId);
        System.out.println(totalRating);
        System.out.println(values);

        return values.divide(totalRating, 1, RoundingMode.HALF_UP);
    }
}
