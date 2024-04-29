package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Review;
import com.example.agriecommerce.utils.ReviewStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Page<Review>> findByProductId(Long productId, Pageable pageable);

    Long countByProductId(Long productId);

    @Query(value = "SELECT COUNT(*) FROM tbl_review " +
            "WHERE product_id = :productId " +
            "AND rating BETWEEN :ratingA AND :ratingB " +
            "GROUP BY rating",
            nativeQuery = true)
    Long countByRating(@Param("productId") Long productId, @Param("ratingA") Integer ratingA, @Param("ratingB") Double ratingB);

    @Query(value = "SELECT COALESCE(SUM(rating), 0) FROM tbl_review WHERE product_id = :productId", nativeQuery = true)
    BigDecimal getTotalRating(@Param("productId") Long productId);

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    Review findByUserIdAndProductId(Long userId, Long productId);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM tbl_review r " +
            "LEFT JOIN tbl_product p ON r.product_id = p.id " +
            "WHERE p.supplier_id = :supplierId")
    Long countBySupplierId(@Param("supplierId") Long supplierId);
    @Query(nativeQuery = true, value = "SELECT COALESCE(SUM(rating), 0) " +
            "FROM tbl_review r " +
            "LEFT JOIN tbl_product p ON r.product_id = p.id " +
            "WHERE p.supplier_id = :supplierId")
    BigDecimal getSupplierRating(@Param("supplierId") Long supplierId);

    @Query(nativeQuery = true, value = "SELECT r.id as id, r.feed_back as feedBack, r.rating as rating, " +
            "r.review_date as reviewDate, r.product_id as productId, u.full_name as fullName, p.product_name as productName " +
            "FROM tbl_review r " +
            "LEFT JOIN tbl_user u ON r.user_id = u.id " +
            "LEFT JOIN tbl_product p ON r.product_id = p.id " +
            "WHERE p.supplier_id = :supplierId " +
            "ORDER BY r.review_date DESC")
    Optional<List<ReviewStatistic>> getReviewStatistic(@Param("supplierId") Long supplierId);
}
