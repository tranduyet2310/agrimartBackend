package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    Optional<List<SubCategory>> findByCategoryId(Long id);
}
