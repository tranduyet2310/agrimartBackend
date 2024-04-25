package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Order;
import com.example.agriecommerce.utils.OrderInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Page<Order>> findByUserId(Long userId, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT COUNT(o.id) FROM tbl_order o " +
            "LEFT JOIN tbl_order_detail od ON o.id = od.order_id " +
            "WHERE o.user_id = :userId AND od.product_id = :productId AND o.order_status = 3")
    Integer hasUserPurchasedProduct(@Param("userId") Long userId, @Param("productId") Long productId);
    @Query(nativeQuery = true, value = "SELECT o.id as id, o.date_created as dateCreated, o.order_status as orderStatus, " +
            "o.address_id as addressId, o.user_id as userId, od.quantity as quantity, od.product_id as productId, " +
            "p.product_name as productName, p.standard_price as standardPrice, p.discount_price as discountPrice, " +
            "p.product_image as productImage, o.order_number as orderNumber, w.name as warehouseName, od.quantity as quantity " +
            "FROM tbl_order o " +
            "CROSS JOIN tbl_order_detail od ON o.id = od.order_id " +
            "LEFT JOIN tbl_product p ON p.id = od.product_id " +
            "LEFT JOIN tbl_warehouse w on p.warehouse_id = w.id " +
            "WHERE p.supplier_id = :supplierId AND date_created like :date")
    Optional<Page<OrderInfoDto>> findOrderBySupplier(@Param("supplierId") Long supplierId,
                                                     @Param("date") String date, Pageable pageable);
}
