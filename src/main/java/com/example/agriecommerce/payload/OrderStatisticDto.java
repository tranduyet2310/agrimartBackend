package com.example.agriecommerce.payload;

import com.example.agriecommerce.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatisticDto {
    private Long id;
    private OrderStatus orderStatus;
    private Long total;
    private String userFullName;
    private String productName;
    private Long standardPrice;
    private Long discountPrice;
    private String productImage;
    private Long quantity;
}
