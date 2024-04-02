package com.example.agriecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String productName;
    private String description;
    private Long standardPrice;
    private Long discountPrice;
    private Integer quantity;
    private boolean isActive;
    private boolean isNew;
    private boolean isAvailable;
    private String categoryName;
    private String subCategoryName;
    private String warehouseName;
    private String supplierShopName;
    private List<ImageDto> images;
    private Long sold;
    private String supplierProvince;
    private String supplierId;
}
