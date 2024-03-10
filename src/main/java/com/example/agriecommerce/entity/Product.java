package com.example.agriecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_name", nullable = false)
    private String productName;
    @Column(name = "product_image", nullable = false)
    private String productImage;
    private String description;
    @Column(name = "standard_price", nullable = false)
    private Long standardPrice;
    @Column(name = "discount_price")
    private Long discountPrice;
    private Integer quantity;
    @Column(name = "is_active")
    private Integer isActive;
    @Column(name = "is_new")
    private Integer isNew;
}
