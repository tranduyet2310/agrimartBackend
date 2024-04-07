package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.FieldDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldDetailRepository extends JpaRepository<FieldDetail, Long> {
    Integer countByFieldId(Long fieldId);
}
