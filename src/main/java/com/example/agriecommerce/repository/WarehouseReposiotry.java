package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Product;
import com.example.agriecommerce.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WarehouseReposiotry extends JpaRepository<Warehouse, Long> {
    Optional<List<Warehouse>> findBySupplierId(Long supplierId);
    Optional<Warehouse> findByWarehouseNameAndSupplierId(String warehouseName, Long supplierId);

    Optional<Page<Warehouse>> findBySupplierId(Long supplierId, Pageable pageable);
    @Query("SELECT w FROM Warehouse w WHERE " +
            "w.supplier.id = :supplierId AND w.warehouseName LIKE CONCAT('%', :query, '%') OR " +
            "w.contact LIKE CONCAT('%', :query, '%')")
    Optional<Page<Warehouse>> searchWarehouse(String query, Long supplierId, Pageable pageable);
}
