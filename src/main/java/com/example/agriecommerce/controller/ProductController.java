package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.ProductDto;
import com.example.agriecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/suppliers/{id}/products")
    public ResponseEntity<ProductDto> createProduct(@PathVariable("id") Long supplierId,
                                                    @RequestParam("productName") String productName,
                                                    @RequestParam("description") String description,
                                                    @RequestParam("standardPrice") Long standardPrice,
                                                    @RequestParam("discountPrice") Long discountPrice,
                                                    @RequestParam("quantity") Integer quantity,
                                                    @RequestParam("isActive") Boolean isActive,
                                                    @RequestParam("isNew") Boolean isNew,
                                                    @RequestParam("isAvailable") Boolean isAvailable,
                                                    @RequestParam("categoryName") String categoryName,
                                                    @RequestParam("subCategoryName") String subCategoryName,
                                                    @RequestParam("warehouseName") String warehouseName,
                                                    @RequestParam("file") MultipartFile[] multipartFiles) {
        ProductDto productDto = new ProductDto();
        productDto.setProductName(productName);
        productDto.setDescription(description);
        productDto.setStandardPrice(standardPrice);
        productDto.setDiscountPrice(discountPrice);
        productDto.setQuantity(quantity);
        productDto.setActive(isActive);
        productDto.setNew(isNew);
        productDto.setAvailable(isAvailable);
        productDto.setCategoryName(categoryName);
        productDto.setSubCategoryName(subCategoryName);
        productDto.setWarehouseName(warehouseName);

        List<MultipartFile> files = new ArrayList<>(Arrays.asList(multipartFiles));

        ProductDto createdProduct = productService.createProduct(supplierId, productDto, files);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("/suppliers/{id}/products")
    public ResponseEntity<List<ProductDto>> getProductBySupplierId(@PathVariable("id") Long supplierId) {
        return ResponseEntity.ok(productService.getProductBySupplierId(supplierId));
    }

    @PatchMapping("/suppliers/{supplierId}/products/{productId}/active")
    public ResponseEntity<ProductDto> activeProduct(@PathVariable("supplierId") Long supplierId,
                                                    @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.activeProduct(supplierId, productId));
    }

    @PatchMapping("/suppliers/{supplierId}/products/{productId}/inactive")
    public ResponseEntity<ProductDto> inactiveProduct(@PathVariable("supplierId") Long supplierId,
                                                      @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.inactiveProduct(supplierId, productId));
    }

    @PutMapping("/suppliers/{supplierId}/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("supplierId") Long supplierId,
                                                    @PathVariable("productId") Long productId,
                                                    @RequestParam("productName") String productName,
                                                    @RequestParam("description") String description,
                                                    @RequestParam("standardPrice") Long standardPrice,
                                                    @RequestParam("discountPrice") Long discountPrice,
                                                    @RequestParam("quantity") Integer quantity,
                                                    @RequestParam("isActive") Boolean isActive,
                                                    @RequestParam("isNew") Boolean isNew,
                                                    @RequestParam("isAvailable") Boolean isAvailable,
                                                    @RequestParam("categoryName") String categoryName,
                                                    @RequestParam("subCategoryName") String subCategoryName,
                                                    @RequestParam("warehouseName") String warehouseName,
                                                    @RequestParam("file") MultipartFile[] multipartFiles) {
        ProductDto productDto = new ProductDto();
        productDto.setProductName(productName);
        productDto.setDescription(description);
        productDto.setStandardPrice(standardPrice);
        productDto.setDiscountPrice(discountPrice);
        productDto.setQuantity(quantity);
        productDto.setActive(isActive);
        productDto.setNew(isNew);
        productDto.setAvailable(isAvailable);
        productDto.setCategoryName(categoryName);
        productDto.setSubCategoryName(subCategoryName);
        productDto.setWarehouseName(warehouseName);

        List<MultipartFile> files = new ArrayList<>(Arrays.asList(multipartFiles));

        return ResponseEntity.ok(productService.updateProduct(supplierId, productId, productDto, files));
    }

    @DeleteMapping("/suppliers/{supplierId}/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("supplierId") Long supplierId,
                                                @PathVariable("productId") Long productId) {
        productService.deleteProduct(supplierId, productId);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
