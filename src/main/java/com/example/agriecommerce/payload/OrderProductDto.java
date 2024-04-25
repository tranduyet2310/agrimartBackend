package com.example.agriecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDto {
    private long productId;
    private String productName;
    private long standardPrice;
    private long discountPrice;
    private String productImage;
    private String warehouseName;
    private long quantity;
}
