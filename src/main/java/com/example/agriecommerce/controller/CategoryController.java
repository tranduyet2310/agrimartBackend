package com.example.agriecommerce.controller;

import com.example.agriecommerce.exception.CloudinaryException;
import com.example.agriecommerce.payload.CategoryRequest;
import com.example.agriecommerce.payload.CategoryResponse;
import com.example.agriecommerce.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    private CategoryService categoryService;
//    private ObjectMapper objectMapper;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestParam("categoryName") String categoryName,
                                                           @RequestParam("file") MultipartFile multipartFile) throws IOException {
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if (bi == null) {
            throw new CloudinaryException("Image not valid");
        }
        CategoryResponse response = categoryService.createCategory(categoryName, multipartFile);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> responses = categoryService.getAllCategories();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
                                                           @RequestParam("categoryName") String categoryName,
                                                           @RequestParam("file") MultipartFile multipartFile,
                                                           @RequestParam("isUpdate") Boolean isUpdated) {
        CategoryResponse response = categoryService.updateCategory(categoryName, multipartFile, id, isUpdated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}
