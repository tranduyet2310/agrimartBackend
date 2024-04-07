package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, Long> {
    Optional<List<Field>> findBySupplierId(Long supplierId);
}
