package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<List<Product>> findBySupplierId(Long supplierId);
}
