package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Category;
import com.example.agriecommerce.entity.SubCategory;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.SubCategoryDto;
import com.example.agriecommerce.repository.CategoryRepository;
import com.example.agriecommerce.repository.SubCategoryRepository;
import com.example.agriecommerce.service.SubCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubcategoryServiceImpl implements SubCategoryService {
    private SubCategoryRepository subCategoryRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    @Autowired
    public SubcategoryServiceImpl(SubCategoryRepository subCategoryRepository,
                                  CategoryRepository categoryRepository,
                                  ModelMapper modelMapper) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SubCategoryDto createSubcategory(SubCategoryDto subCategoryDto) {
        SubCategory subCategory = new SubCategory();
        subCategory.setSubcategoryName(subCategoryDto.getSubcategoryName());
        Category category = categoryRepository.findByCategoryName(subCategoryDto.getCategoryName()).orElseThrow(
                () -> new ResourceNotFoundException("Category does not exists")
        );
        subCategory.setCategory(category);

        SubCategory savedSubcategory = subCategoryRepository.save(subCategory);

        return modelMapper.map(savedSubcategory, SubCategoryDto.class);
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
