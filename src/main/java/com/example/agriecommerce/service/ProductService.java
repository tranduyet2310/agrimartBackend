package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ProductService {
    ProductDto createProduct(Long supplierId, ProductDto productDto, List<MultipartFile> files);
    ProductDto createProductV2(Long supplierId, ProductDto productDto, List<MultipartFile> files);
    ProductDto getProductById(Long productId);
    ProductResponse getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getProductBySupplierIdV2(Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getProductBySupplierId(Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getProductByCategoryId(Long categoryId, int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getProductBySubcategoryId(Long subcategoryId, int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getProductsWithDiscount(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getUpcomingProducts(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse searchProduct(String query, int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse searchProductBySupplier(String query, Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir);
    ProductDto updateProduct(Long supplierId, Long productId, ProductDto productDto, List<MultipartFile> files);
    ProductDto updateProductState(Long productId);
    ProductDto updateProductInfo(Long supplierId, Long productId, ProductDto productDto);
    ResultDto deleteProduct(Long supplierId, Long productId);
    ProductDto activeProduct(Long suppilerId, Long productId);
    ProductDto inactiveProduct(Long suppilerId, Long productId);
    ProductDto increaseSoldNumber(Long productId, Long quantity);
    ProductDto decreaseQuantity(Long productId, Integer quantity);
    List<CategoryDto> getCategoryBySupplierId(Long supplierId);
    ResultDto countTotalProducts(Long supplierId);
    ResultDto countSoldProducts(Long supplierId);
}
