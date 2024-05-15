package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.service.ProductService;
import com.example.agriecommerce.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize(("hasRole('SUPPLIER')"))
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
        productDto.setProductName(productName.substring(1, productName.length() - 1));
        productDto.setDescription(description.substring(1, description.length() - 1));
        productDto.setStandardPrice(standardPrice);
        productDto.setDiscountPrice(discountPrice);
        productDto.setQuantity(quantity);
        productDto.setActive(isActive);
        productDto.setNew(isNew);
        productDto.setAvailable(isAvailable);
        productDto.setCategoryName(categoryName.substring(1, categoryName.length() - 1));
        productDto.setSubCategoryName(subCategoryName.substring(1, subCategoryName.length() - 1));
        productDto.setWarehouseName(warehouseName.substring(1, warehouseName.length() - 1));

        List<MultipartFile> files = new ArrayList<>(Arrays.asList(multipartFiles));

        ProductDto createdProduct = productService.createProduct(supplierId, productDto, files);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/v2")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<ProductDto> createProduct_Encrypt(@PathVariable("id") Long supplierId,
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
        productDto.setProductName(productName.substring(1, productName.length() - 1));
        productDto.setDescription(description.substring(1, description.length() - 1));
        productDto.setStandardPrice(standardPrice);
        productDto.setDiscountPrice(discountPrice);
        productDto.setQuantity(quantity);
        productDto.setActive(isActive);
        productDto.setNew(isNew);
        productDto.setAvailable(isAvailable);
        productDto.setCategoryName(categoryName.substring(1, categoryName.length() - 1));
        productDto.setSubCategoryName(subCategoryName.substring(1, subCategoryName.length() - 1));
        productDto.setWarehouseName(warehouseName.substring(1, warehouseName.length() - 1));

        List<MultipartFile> files = new ArrayList<>(Arrays.asList(multipartFiles));

        ProductDto createdProduct = productService.createProductV2(supplierId, productDto, files);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("all")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(productService.getAllProducts(pageNo, pageSize, sortBy, sortDir));
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

    @GetMapping("suppliers/{supplierId}/search")
    public ResponseEntity<ProductResponse> searchProductBySupplier(
            @PathVariable("supplierId") Long supplierId,
            @RequestParam(value = "query") String query,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(productService.searchProductBySupplier(query, supplierId, pageNo, pageSize, sortBy, sortDir));
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

    @GetMapping("suppliers/{id}/v2")
    public ResponseEntity<ProductResponse> getProductBySupplierId_Encrypt(
            @PathVariable("id") Long supplierId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return ResponseEntity.ok(productService.getProductBySupplierIdV2(supplierId, pageNo, pageSize, sortBy, sortDir));
    }

    @PatchMapping("{supplierId}/{productId}/active")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<ProductDto> activeProduct(@PathVariable("supplierId") Long supplierId,
                                                    @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.activeProduct(supplierId, productId));
    }

    @PatchMapping("{supplierId}/{productId}/inactive")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<ProductDto> inactiveProduct(@PathVariable("supplierId") Long supplierId,
                                                      @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.inactiveProduct(supplierId, productId));
    }

    @PutMapping("{supplierId}/{productId}")
    @PreAuthorize(("hasRole('SUPPLIER')"))
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
        productDto.setProductName(productName.substring(1, productName.length() - 1));
        productDto.setDescription(description.substring(1, description.length() - 1));
        productDto.setStandardPrice(standardPrice);
        productDto.setDiscountPrice(discountPrice);
        productDto.setQuantity(quantity);
        productDto.setActive(isActive);
        productDto.setNew(isNew);
        productDto.setAvailable(isAvailable);
        productDto.setCategoryName(categoryName.substring(1, categoryName.length() - 1));
        productDto.setSubCategoryName(subCategoryName.substring(1, subCategoryName.length() - 1));
        productDto.setWarehouseName(warehouseName.substring(1, warehouseName.length() - 1));

        List<MultipartFile> files = new ArrayList<>(Arrays.asList(multipartFiles));

        return ResponseEntity.ok(productService.updateProduct(supplierId, productId, productDto, files));
    }

    @PatchMapping("{supplierId}/{productId}/info")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<ProductDto> updateProductInfo(@PathVariable("supplierId") Long supplierId,
                                                        @PathVariable("productId") Long productId,
                                                        @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProductInfo(supplierId, productId, productDto));
    }

    @DeleteMapping("{supplierId}/{productId}")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<ResultDto> deleteProduct(@PathVariable("supplierId") Long supplierId,
                                                   @PathVariable("productId") Long productId) {
        ResultDto result = productService.deleteProduct(supplierId, productId);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("{productId}")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<ProductDto> increaseSoldNumber(@PathVariable("productId") Long productId,
                                                         @RequestParam("quantity") Long quantity) {
        return ResponseEntity.ok(productService.increaseSoldNumber(productId, quantity));
    }

    @PatchMapping("{productId}/quantity")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<ProductDto> decreaseQuantity(@PathVariable("productId") Long productId,
                                                         @RequestParam("quantity") Integer quantity) {
        return ResponseEntity.ok(productService.decreaseQuantity(productId, quantity));
    }

    @GetMapping("category/{supplierId}")
    public ResponseEntity<List<CategoryDto>> getCategoryBySupplierId(@PathVariable("supplierId") Long supplierId) {
        return ResponseEntity.ok(productService.getCategoryBySupplierId(supplierId));
    }

    @GetMapping("{supplierId}/total")
    public ResponseEntity<ResultDto> getTotalProductBySupplier(@PathVariable("supplierId") Long supplierId) {
        return ResponseEntity.ok(productService.countTotalProducts(supplierId));
    }

    @GetMapping("{supplierId}/sold")
    public ResponseEntity<ResultDto> getSoldProductBySupplier(@PathVariable("supplierId") Long supplierId) {
        return ResponseEntity.ok(productService.countSoldProducts(supplierId));
    }

    @PatchMapping("{productId}/state")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<ProductDto> updateProductState(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.updateProductState(productId));
    }
}
