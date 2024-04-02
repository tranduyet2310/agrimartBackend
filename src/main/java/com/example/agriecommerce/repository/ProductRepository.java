package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<List<Product>> findBySupplierId(Long supplierId);

    Optional<Page<Product>> findByCategoryId(Long categoryId, Pageable pageable);

    Optional<Page<Product>> findBySubCategoryId(Long subcategoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.discountPrice > 0")
    Optional<Page<Product>> findProductsWithDiscount(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isNew = true")
    Optional<Page<Product>> findUpcomingProduct(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "p.productName LIKE CONCAT('%', :query, '%') OR " +
            "p.supplier.shopName LIKE CONCAT('%', :query, '%')")
    Optional<Page<Product>> searchProduct(String query, Pageable pageable);
//    @Query("SELECT p, CASE WHEN f.product")
//    Optional<Page<Product>> findProductsWithUserId(Long userId, Pageable pageable);
}
