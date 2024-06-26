package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Category;
import com.example.agriecommerce.entity.Image;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.CategoryDto;
import com.example.agriecommerce.payload.CategoryResponse;
import com.example.agriecommerce.repository.CategoryRepository;
import com.example.agriecommerce.repository.ImageRepository;
import com.example.agriecommerce.service.CategoryService;
import com.example.agriecommerce.service.CloudinaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    @Autowired
    public CategoryServiceImpl(ModelMapper modelMapper,
                               CategoryRepository categoryRepository,
                               CloudinaryService cloudinaryService,
                               ImageRepository imageRepository) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
    }

    @Override
    @Transactional
    public CategoryDto createCategory(String categoryName, MultipartFile file) {
        Category category = new Category();
        category.setCategoryName(categoryName);

        Map result = cloudinaryService.upload(file);
        category.setCategoryImage((String) result.get("secure_url"));
        // save to images table
        Image image = new Image((String) result.get("original_filename"),
                (String) result.get("secure_url"),
                (String) result.get("public_id"));
        imageRepository.save(image);

        Category savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<Category> categories = categoryPage.getContent();
        List<CategoryDto> content = categories.stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(content);
        categoryResponse.setPageNo(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPage(categoryPage.getTotalPages());
        categoryResponse.setLast(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(String categoryName, MultipartFile file, Long id, Boolean isUpdated) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("category", "id", id)
        );

        category.setId(id);
        category.setCategoryName(categoryName);

        if (isUpdated) {
            String imageUrl = category.getCategoryImage();
            Image oldImage = imageRepository.findByImageUrl(imageUrl).orElseThrow(
                    () -> new ResourceNotFoundException("Image does not exists")
            );

            String cloudinaryImageId = oldImage.getImageId();
            cloudinaryService.delete(cloudinaryImageId);
            imageRepository.delete(oldImage);

            Map result = cloudinaryService.upload(file);
            category.setCategoryImage((String) result.get("secure_url"));
            Image newImage = new Image((String) result.get("original_filename"),
                    (String) result.get("secure_url"),
                    (String) result.get("public_id"));
            imageRepository.save(newImage);
        }

        Category updatedCategory = categoryRepository.save(category);

        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    @Transactional
    public CategoryDto updateCategoryInfo(String categoryName, Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("category", "id", id)
        );

        category.setId(id);
        category.setCategoryName(categoryName);
        Category updatedCategory = categoryRepository.save(category);

        return modelMapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("category", "id", id)
        );
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("category", "id", id)
        );
        String imageUrl = category.getCategoryImage();
        Image image = imageRepository.findByImageUrl(imageUrl).orElseThrow(
                () -> new ResourceNotFoundException("Image does not exists")
        );

        String cloudinaryImageId = image.getImageId();
        cloudinaryService.delete(cloudinaryImageId);
        imageRepository.delete(image);

        categoryRepository.delete(category);
    }
}
