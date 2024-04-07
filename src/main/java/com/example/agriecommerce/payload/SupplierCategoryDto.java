package com.example.agriecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierCategoryDto {
    private Long categoryId;
    private Long subcategoryId;
    private String imageUrl;
    private String categoryName;
    private String subcategoryName;
}
