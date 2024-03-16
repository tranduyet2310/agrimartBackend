package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(Long supplierId, ProductDto productDto, List<MultipartFile> files);

    ProductDto getProductById(Long productId);

    List<ProductDto> getProductBySupplierId(Long supplierId);

    ProductDto updateProduct(Long supplierId, Long productId, ProductDto productDto, List<MultipartFile> files);

    void deleteProduct(Long supplierId, Long productId);

    ProductDto activeProduct(Long suppilerId, Long productId);

    ProductDto inactiveProduct(Long suppilerId, Long productId);
}
