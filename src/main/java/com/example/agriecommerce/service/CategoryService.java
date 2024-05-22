package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.CategoryDto;
import com.example.agriecommerce.payload.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(String categoryName, MultipartFile file);
    CategoryDto updateCategory(String categoryName, MultipartFile file, Long id, Boolean isUpdated);
    CategoryDto updateCategoryInfo(String categoryName, Long id);
    CategoryDto getCategoryById(Long id);
    List<CategoryDto> getAllCategories();
    CategoryResponse getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir);
    void deleteCategory(Long id);

}
