package com.example.agriecommerce.payload;

import com.example.agriecommerce.entity.Order;
import com.example.agriecommerce.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private Long id;
    private Integer quantity;
    private Long orderId;
    private Long productId;
    private ProductDto product;
}
