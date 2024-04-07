package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.service.ProductService;
import com.example.agriecommerce.utils.AppConstants;
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
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("suppliers/{id}")
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

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("category")
    public ResponseEntity<ProductResponse> getProductByCategoryId(
            @RequestParam("id") Long categoryId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(productService.getProductByCategoryId(categoryId, pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("subcategory")
    public ResponseEntity<ProductResponse> getProductBySubCategoryId(
            @RequestParam("id") Long subcategoryId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(productService.getProductBySubcategoryId(subcategoryId, pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("discount")
    public ResponseEntity<ProductResponse> getProductsWithDiscount(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(productService.getProductsWithDiscount(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("upcoming")
    public ResponseEntity<ProductResponse> getUpcomingProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(productService.getUpcomingProducts(pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("search")
    public ResponseEntity<ProductResponse> searchProduct(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(productService.searchProduct(query, pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("suppliers/{id}")
    public ResponseEntity<ProductResponse> getProductBySupplierId(
            @PathVariable("id") Long supplierId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return ResponseEntity.ok(productService.getProductBySupplierId(supplierId, pageNo, pageSize, sortBy, sortDir));
    }

    @PatchMapping("{supplierId}/{productId}/active")
    public ResponseEntity<ProductDto> activeProduct(@PathVariable("supplierId") Long supplierId,
                                                    @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.activeProduct(supplierId, productId));
    }

    @PatchMapping("{supplierId}/{productId}/inactive")
    public ResponseEntity<ProductDto> inactiveProduct(@PathVariable("supplierId") Long supplierId,
                                                      @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.inactiveProduct(supplierId, productId));
    }

    @PutMapping("{supplierId}/{productId}")
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

    @DeleteMapping("{supplierId}/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("supplierId") Long supplierId,
                                                @PathVariable("productId") Long productId) {
        productService.deleteProduct(supplierId, productId);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @PatchMapping("{productId}")
    public ResponseEntity<ProductDto> increaseSoldNumber(@PathVariable("productId") Long productId,
                                                         @RequestParam("quantity") Long quantity) {
        return ResponseEntity.ok(productService.increaseSoldNumber(productId, quantity));
    }

    @GetMapping("category/{supplierId}")
    public ResponseEntity<List<CategoryDto>> getCategoryBySupplierId(@PathVariable("supplierId") Long supplierId){
        return ResponseEntity.ok(productService.getCategoryBySupplierId(supplierId));
    }

    @GetMapping("{supplierId}/total")
    public ResponseEntity<ResultDto> getTotalProductBySupplier(@PathVariable("supplierId") Long supplierId){
        return ResponseEntity.ok(productService.countTotalProducts(supplierId));
    }

    @GetMapping("{supplierId}/sold")
    public ResponseEntity<ResultDto> getSoldProductBySupplier(@PathVariable("supplierId") Long supplierId){
        return ResponseEntity.ok(productService.countSoldProducts(supplierId));
    }
}
