package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.SubCategoryDto;
import com.example.agriecommerce.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
public class SubCategoryController {
    private SubCategoryService subCategoryService;

    @Autowired
    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @PostMapping("{id}/subcategories")
    public ResponseEntity<SubCategoryDto> createSubcategory(@PathVariable("id") Long categoryId,
                                                            @RequestBody SubCategoryDto subCategoryDto) {
        SubCategoryDto dto = subCategoryService.createSubcategory(categoryId, subCategoryDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/subcategories/{id}")
    public ResponseEntity<SubCategoryDto> getSubcategoryById(@PathVariable("id") Long subcategoryId) {
        return ResponseEntity.ok(subCategoryService.getSubcategoryById(subcategoryId));
    }

    @GetMapping("{id}/subcategories")
    public ResponseEntity<List<SubCategoryDto>> getSubcategoryByCategoryId(@PathVariable("id") Long categoryId) {
        return ResponseEntity.ok(subCategoryService.getSubcategoryByCategoryId(categoryId));
    }

    @DeleteMapping("subcategories/{id}")
    public ResponseEntity<String> deleteSubcategory(@PathVariable("id") Long subcategoryId) {
        subCategoryService.deleteSubcategory(subcategoryId);
        return ResponseEntity.ok("Subcategory deleted successfully");
    }

    @PutMapping("{categoryId}/subcategories/{id}")
    public ResponseEntity<SubCategoryDto> updateCategory(@PathVariable("categoryId") Long categoryId,
                                                         @PathVariable("id") Long subcategoryId,
                                                         @RequestBody SubCategoryDto subCategoryDto) {
        return ResponseEntity.ok(subCategoryService.updateSubcategory(subcategoryId, subCategoryDto, categoryId));
    }
}
