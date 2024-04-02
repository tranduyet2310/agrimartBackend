package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.ProductDto;
import com.example.agriecommerce.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(Long supplierId, ProductDto productDto, List<MultipartFile> files);

    ProductDto getProductById(Long productId);

    List<ProductDto> getProductBySupplierId(Long supplierId);

    ProductResponse getProductByCategoryId(Long categoryId, int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getProductBySubcategoryId(Long subcategoryId, int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getProductsWithDiscount(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse getUpcomingProducts(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductResponse searchProduct(String query, int pageNo, int pageSize, String sortBy, String sortDir);

    ProductDto updateProduct(Long supplierId, Long productId, ProductDto productDto, List<MultipartFile> files);

    void deleteProduct(Long supplierId, Long productId);

    ProductDto activeProduct(Long suppilerId, Long productId);

    ProductDto inactiveProduct(Long suppilerId, Long productId);
}
