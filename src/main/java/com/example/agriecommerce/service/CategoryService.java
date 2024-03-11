package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.CategoryRequest;
import com.example.agriecommerce.payload.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(String categoryName, MultipartFile file);
    CategoryResponse updateCategory(String categoryName, MultipartFile file, Long id, Boolean isUpdated);
    CategoryResponse getCategoryById(Long id);
    List<CategoryResponse> getAllCategories();
    void deleteCategory(Long id);

}
