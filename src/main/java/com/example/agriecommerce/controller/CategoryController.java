package com.example.agriecommerce.controller;

import com.example.agriecommerce.exception.CloudinaryException;
import com.example.agriecommerce.payload.CategoryDto;
import com.example.agriecommerce.payload.CategoryResponse;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.service.CategoryService;
import com.example.agriecommerce.utils.AppConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
@Tag(name = "REST APIs for Category")
@RestController
@RequestMapping("api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestParam("categoryName") String categoryName,
                                                      @RequestParam("file") MultipartFile multipartFile) throws IOException {
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if (bi == null) {
            throw new CloudinaryException("Image format is not valid");
        }
        CategoryDto response = categoryService.createCategory(categoryName, multipartFile);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> responses = categoryService.getAllCategories();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("list")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(categoryService.getAllCategories(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id,
                                                      @RequestParam("categoryName") String categoryName,
                                                      @RequestParam("file") MultipartFile multipartFile,
                                                      @RequestParam("isUpdate") Boolean isUpdated) {
        CategoryDto response = categoryService.updateCategory(categoryName, multipartFile, id, isUpdated);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{id}/info")
    public ResponseEntity<CategoryDto> updateCategoryInfo(@PathVariable Long id,
                                                      @RequestParam("categoryName") String categoryName) {
        CategoryDto response = categoryService.updateCategoryInfo(categoryName, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResultDto> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new ResultDto(true, "Category deleted successfully"));
    }
}
