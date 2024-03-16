package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<List<Review>> findByProductId(Long productId);

    Long countByProductId(Long productId);
    @Query(value = "SELECT COUNT(*) FROM tbl_review WHERE product_id = :productId AND rating = :rating GROUP BY rating",
            nativeQuery = true)
    Long countByRating(@Param("productId") Long productId, @Param("rating") Integer rating);

    @Query(value = "SELECT COALESCE(SUM(rating), 0) FROM tbl_review WHERE product_id = :productId", nativeQuery = true)
    BigDecimal getTotalRating(@Param("productId") Long productId);

}
