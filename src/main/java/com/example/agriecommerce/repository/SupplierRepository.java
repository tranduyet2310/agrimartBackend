package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<Supplier> findByPublicKey(String publicKey);
    Optional<Page<Supplier>> findByIsActive(Boolean isActive, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM tbl_supplier WHERE is_active = 0")
    long countRegisterAccount();
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM tbl_supplier WHERE is_active = 1")
    long countTotalSupplier();
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM tbl_supplier s WHERE MONTH(s.date_created) = :month AND YEAR(s.date_created) = :year")
    long countSuppliersByMonthAndYear(@Param("month") int month, @Param("year") int year);
}
