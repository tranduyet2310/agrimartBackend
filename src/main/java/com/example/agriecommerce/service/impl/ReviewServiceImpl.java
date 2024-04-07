package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.*;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.repository.ProductRepository;
import com.example.agriecommerce.repository.ReviewRepository;
import com.example.agriecommerce.repository.SupplierRepository;
import com.example.agriecommerce.repository.UserRepository;
import com.example.agriecommerce.service.ReviewService;
import com.example.agriecommerce.utils.DateTimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final SupplierRepository supplierRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             ProductRepository productRepository,
                             ModelMapper modelMapper,
                             SupplierRepository supplierRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public ReviewDto createReview(Long productId, Long userId, ReviewDto reviewDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );

        Review review;
        boolean check = reviewRepository.existsByUserIdAndProductId(userId, productId);
        if (check){
            review = reviewRepository.findByUserIdAndProductId(userId, productId);
        } else {
            review = new Review();
        }

        review.setUser(user);
        review.setProduct(product);
        review.setFeedBack(reviewDto.getFeedBack());
        review.setRating(reviewDto.getRating());

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
    public ReviewResponse getAllReviewsByProductId(Long productId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Review> reviewsPage = reviewRepository.findByProductId(productId, pageable).orElseThrow(
                () -> new ResourceNotFoundException("product does not have any review " + productId)
        );

        List<Review> reviews = reviewsPage.getContent();
        List<ReviewDto> content = reviews.stream().map(review -> modelMapper.map(review, ReviewDto.class)).collect(Collectors.toList());

        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setContent(content);
        reviewResponse.setPageNo(reviewsPage.getNumber());
        reviewResponse.setPageSize(reviewsPage.getSize());
        reviewResponse.setTotalElements(reviewsPage.getTotalElements());
        reviewResponse.setTotalPage(reviewsPage.getTotalPages());
        reviewResponse.setLast(reviewsPage.isLast());

        return reviewResponse;
    }

    @Override
    public ReviewStatisticDto calculateTotalRating(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );
        Long total = reviewRepository.countByProductId(productId);
        ReviewStatisticDto dto = new ReviewStatisticDto();
        dto.setTotalReviews(total);

        return dto;
    }

    @Override
    public ReviewStatisticDto statisticRating(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );
        ReviewStatisticDto dto = new ReviewStatisticDto();
        for (int i = 1; i <= 5; i++) {
            double b = (i+0.5);
            Long count = reviewRepository.countByRating(productId, i, b);
            if (ObjectUtils.isEmpty(count)) count = 0L;
            switch (i) {
                case 1 -> dto.setOneStar(count);
                case 2 -> dto.setTwoStar(count);
                case 3 -> dto.setThreeStar(count);
                case 4 -> dto.setFourStar(count);
                case 5 -> dto.setFiveStar(count);
            }
        }

        Long total = reviewRepository.countByProductId(productId);
        dto.setTotalReviews(total);

        if (total == 0){
            dto.setAverageRating(BigDecimal.valueOf(0));
        } else {
            BigDecimal totalRating = BigDecimal.valueOf(total);
            BigDecimal values = reviewRepository.getTotalRating(productId);
            dto.setAverageRating(values.divide(totalRating, 1, RoundingMode.HALF_UP));
        }

        return dto;
    }

    @Override
    public ReviewStatisticDto averageRating(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product", "id", productId)
        );

        ReviewStatisticDto dto = new ReviewStatisticDto();
        Long total = reviewRepository.countByProductId(productId);
        if (total == 0){
            dto.setAverageRating(BigDecimal.valueOf(0));
        } else {
            BigDecimal totalRating = BigDecimal.valueOf(total);
            BigDecimal values = reviewRepository.getTotalRating(productId);
            dto.setAverageRating(values.divide(totalRating, 1, RoundingMode.HALF_UP));
        }

        return dto;
    }

    @Override
    public ReviewStatisticDto supplierAverageRating(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplierId", "id", supplierId)
        );

        ReviewStatisticDto dto = new ReviewStatisticDto();
        Long total = reviewRepository.countBySupplierId(supplierId);
        if (total == 0){
            dto.setAverageRating(BigDecimal.valueOf(0));
        } else {
            BigDecimal totalRating = BigDecimal.valueOf(total);
            BigDecimal values = reviewRepository.getSupplierRating(supplierId);
            dto.setAverageRating(values.divide(totalRating, 1, RoundingMode.HALF_UP));
        }

        return dto;
    }

    @Override
    public ResultDto getTotalReviewsBySupplier(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplierId", "id", supplierId)
        );

        Long result = reviewRepository.countBySupplierId(supplierId);
        if (result == null) result = 0L;
        return new ResultDto(true, result.toString());
    }
}
