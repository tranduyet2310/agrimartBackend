package com.example.agriecommerce.payload;

import com.example.agriecommerce.entity.Field;
import com.example.agriecommerce.entity.OrderStatus;
import com.example.agriecommerce.entity.Supplier;
import com.example.agriecommerce.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CooperationDto {
    private Long id;
    private String fullName;
    private String description;
    private String cropsName;
    private Double requireYield;
    private String investment;
    private String contact;
    private Long userId;
    private Long supplierId;
    private String supplierShopName;
    private String supplierPhone;
    private String supplierContactName;
    private Long fieldId;
    private OrderStatus cooperationStatus;
}