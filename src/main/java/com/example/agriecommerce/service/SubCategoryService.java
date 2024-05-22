package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.payload.SubCategoryDto;

import java.util.List;

public interface SubCategoryService {
    SubCategoryDto createSubcategory(Long categoryId, SubCategoryDto subCategoryDto);
    ResultDto createListSubcategory(Long categoryId, List<String> subcategoryName);

    SubCategoryDto updateSubcategory(Long subcategoryId, SubCategoryDto subCategoryDto, Long categoryId);
    ResultDto updateListSubcategories( List<SubCategoryDto> subCategoryDtoList, Long categoryId);

    SubCategoryDto getSubcategoryById(Long subcategoryId);

    List<SubCategoryDto> getSubcategoryByCategoryId(Long categoryId);

    void deleteSubcategory(Long subcategoryId);
}
