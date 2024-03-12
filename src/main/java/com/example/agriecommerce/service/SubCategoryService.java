package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.SubCategoryDto;

import java.util.List;

public interface SubCategoryService {
    SubCategoryDto createSubcategory(Long categoryId, SubCategoryDto subCategoryDto);

    SubCategoryDto updateSubcategory(Long subcategoryId, SubCategoryDto subCategoryDto, Long categoryId);

    SubCategoryDto getSubcategoryById(Long subcategoryId);

    List<SubCategoryDto> getSubcategoryByCategoryId(Long categoryId);

    void deleteSubcategory(Long subcategoryId);
}
