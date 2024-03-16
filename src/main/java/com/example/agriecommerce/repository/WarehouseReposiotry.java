package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseReposiotry extends JpaRepository<Warehouse, Long> {
    Optional<List<Warehouse>> findBySupplierId(Long supplierId);
    Optional<Warehouse> findByWarehouseName(String warehouseName);
}
