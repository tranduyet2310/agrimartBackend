package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.CategoryDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(String categoryName, MultipartFile file);
    CategoryDto updateCategory(String categoryName, MultipartFile file, Long id, Boolean isUpdated);
    CategoryDto getCategoryById(Long id);
    List<CategoryDto> getAllCategories();
    void deleteCategory(Long id);

}
