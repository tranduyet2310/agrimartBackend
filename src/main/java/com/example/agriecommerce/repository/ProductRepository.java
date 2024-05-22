package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Product;
import com.example.agriecommerce.payload.SupplierCategoryDto;
import com.example.agriecommerce.utils.SupplierCategory;
import jakarta.persistence.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Page<Product>> findBySupplierId(Long supplierId, Pageable pageable);

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

    @Query(nativeQuery = true, value = "SELECT * FROM tbl_product p " +
            "WHERE p.product_name LIKE :query AND p.supplier_id = :supplierId")
    Optional<Page<Product>> searchProductBySupplier(@Param("query") String query,
                                                    @Param("supplierId") Long supplierId, Pageable pageable);

    @Query(value = "SELECT DISTINCT c.id as categoryId, c.image as imageUrl, " +
            "c.name as categoryName, s.id as subcategoryId, s.name  as subcategoryName " +
            "FROM tbl_product p " +
            "LEFT JOIN tbl_category c ON p.category_id = c.id " +
            "LEFT JOIN tbl_subcategory s ON c.id = s.category_id " +
            "WHERE p.supplier_id = :supplierId", nativeQuery = true)
    List<SupplierCategory> getCategoryBySupplierId(@Param("supplierId") Long supplierId);
    @Query(nativeQuery = true, value = "SELECT COALESCE(SUM(sold), 0) FROM tbl_product WHERE supplier_id = :supplierId")
    Long countSoldProduct(@Param("supplierId") Long supplierId);
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM tbl_product WHERE supplier_id = :supplierId")
    Long countProducts(@Param("supplierId") Long supplierId);

    @Query("SELECT COUNT(p) FROM Product p")
    long countAllProducts();
}
