package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.SubCategoryDto;

public interface SubCategoryService {
    SubCategoryDto createSubcategory(SubCategoryDto subCategoryDto);

    SubCategoryDto updateSubcategory(Long id, SubCategoryDto subCategoryDto);

    SubCategoryDto findSubcateogry(Long id);

    void deleteSubcategory(Long id);
}
