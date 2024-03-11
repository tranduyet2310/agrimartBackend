package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.SubCategory;
import com.example.agriecommerce.payload.SubCategoryDto;
import com.example.agriecommerce.repository.CategoryRepository;
import com.example.agriecommerce.repository.SubCategoryRepository;
import com.example.agriecommerce.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubcategoryServiceImpl implements SubCategoryService {
    private SubCategoryRepository subCategoryRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public SubcategoryServiceImpl(SubCategoryRepository subCategoryRepository,
                                  CategoryRepository categoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public SubCategoryDto createSubcategory(SubCategoryDto subCategoryDto) {
        SubCategory subCategory = new SubCategory();
        subCategory.setSubcategoryName(subCategoryDto.getSubcategoryName());

        return null;
    }

    @Override
    public SubCategoryDto updateSubcategory(Long id, SubCategoryDto subCategoryDto) {
        return null;
    }

    @Override
    public SubCategoryDto findSubcateogry(Long id) {
        return null;
    }

    @Override
    public void deleteSubcategory(Long id) {

    }
}
