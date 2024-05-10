package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<Supplier> findByPublicKey(String publicKey);
    Optional<Page<Supplier>> findByIsActive(Boolean isActive, Pageable pageable);

}
