package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, Long> {
    Optional<List<Field>> findBySupplierId(Long supplierId);
    @Query(nativeQuery = true, value = "SELECT * FROM tbl_field WHERE is_complete = :isComplete AND supplier_id= :fieldId")
    Optional<List<Field>> findByIdAndIsComplete(@Param("fieldId") Long fieldId, @Param("isComplete") boolean isComplete);
}
