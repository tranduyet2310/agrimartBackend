package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Category;
import com.example.agriecommerce.entity.SubCategory;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.payload.SubCategoryDto;
import com.example.agriecommerce.repository.CategoryRepository;
import com.example.agriecommerce.repository.SubCategoryRepository;
import com.example.agriecommerce.service.SubCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubcategoryServiceImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SubcategoryServiceImpl(SubCategoryRepository subCategoryRepository,
                                  CategoryRepository categoryRepository,
                                  ModelMapper modelMapper) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public SubCategoryDto createSubcategory(Long categoryId, SubCategoryDto subCategoryDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category does not exists")
        );
        List<SubCategory> subCategories = category.getSubCategories();

        SubCategory subCategory = new SubCategory();
        subCategory.setSubcategoryName(subCategoryDto.getSubcategoryName());
        subCategory.setCategory(category);
        SubCategory savedSubcategory = subCategoryRepository.save(subCategory);

        subCategories.add(savedSubcategory);
        category.setSubCategories(subCategories);
        categoryRepository.save(category);

        return modelMapper.map(savedSubcategory, SubCategoryDto.class);
    }

    @Override
    @Transactional
    public ResultDto createListSubcategory(Long categoryId, List<String> subcategoryNameList) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category does not exists")
        );
        List<SubCategory> subCategories = category.getSubCategories();

        for (String name : subcategoryNameList){
            SubCategory subCategory = new SubCategory();
            subCategory.setSubcategoryName(name);
            subCategory.setCategory(category);
            SubCategory savedSubcategory = subCategoryRepository.save(subCategory);

            subCategories.add(savedSubcategory);
            category.setSubCategories(subCategories);
            categoryRepository.save(category);
        }

        return new ResultDto(true, "create subcategory successfully!");
    }

    @Override
    @Transactional
    public SubCategoryDto updateSubcategory(Long id, SubCategoryDto subCategoryDto, Long categoryId) {
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("subcategory", "id", id)
        );

        subCategory.setId(id);
        subCategory.setSubcategoryName(subCategoryDto.getSubcategoryName());

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("category", "id", categoryId)
        );
        String categoryName = category.getCategoryName();
        String newCategoryName = subCategoryDto.getCategoryName();
        Category newCategory = categoryRepository.findByCategoryName(newCategoryName).orElseThrow(
                () -> new ResourceNotFoundException("Category does not exsist with " + newCategoryName)
        );

        if (!categoryName.equals(newCategoryName)){
            subCategory.setCategory(newCategory);
        } else {
            subCategory.setCategory(category);
        }

        SubCategory updatedCategory = subCategoryRepository.save(subCategory);

        return modelMapper.map(updatedCategory, SubCategoryDto.class);
    }

    @Override
    @Transactional
    public ResultDto updateListSubcategories(List<SubCategoryDto> subCategoryDtoList, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("category", "id", categoryId)
        );
        List<SubCategory> subCategories = category.getSubCategories();
        List<Long> subCategoryIdList = new ArrayList<>();
        for (SubCategory sc : subCategories){
            subCategoryIdList.add(sc.getId());
        }

        if (subCategoryDtoList.isEmpty()){
            for (SubCategory subCategory : subCategories){
                subCategoryRepository.delete(subCategory);
            }
            subCategories.clear();
            categoryRepository.save(category);
        } else {
            for (SubCategoryDto dto : subCategoryDtoList){
                if (subCategoryIdList.contains(dto.getId())){
                    SubCategory subCategory = subCategoryRepository.findById(dto.getId()).orElseThrow(
                            () -> new ResourceNotFoundException("subcategory", "id", dto.getId())
                    );
                    subCategory.setSubcategoryName(dto.getSubcategoryName());
                    subCategoryRepository.save(subCategory);
                } else {
                    SubCategory newSubcategory = new SubCategory();
                    newSubcategory.setCategory(category);
                    newSubcategory.setSubcategoryName(dto.getSubcategoryName());
                    subCategories.add(newSubcategory);
                    subCategoryRepository.save(newSubcategory);
                }
            }
            categoryRepository.save(category);
        }

        return new ResultDto(true, "Update subcategory successfully");
    }

    @Override
    public SubCategoryDto getSubcategoryById(Long id) {
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("subcategory", "id", id)
        );
        return modelMapper.map(subCategory, SubCategoryDto.class);
    }

    @Override
    public List<SubCategoryDto> getSubcategoryByCategoryId(Long categoryId) {
        List<SubCategory> subCategories = subCategoryRepository.findByCategoryId(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("subcategory", "categoryId", categoryId)
        );
        return subCategories.stream()
                .map(subCategory -> modelMapper.map(subCategory, SubCategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSubcategory(Long id) {
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("subcategory", "id", id)
        );
        subCategoryRepository.delete(subCategory);
    }
}
