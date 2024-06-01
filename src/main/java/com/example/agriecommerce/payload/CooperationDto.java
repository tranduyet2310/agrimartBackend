package com.example.agriecommerce.payload;

import com.example.agriecommerce.utils.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private Long addressId;
    private String paymentStatus;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateCreated;
}
