package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.SupplierIntro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierIntroRepository extends JpaRepository<SupplierIntro, Long> {
    Optional<List<SupplierIntro>> findBySupplierId(Long supplierId);
}
