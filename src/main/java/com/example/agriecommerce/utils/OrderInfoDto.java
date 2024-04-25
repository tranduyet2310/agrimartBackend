package com.example.agriecommerce.utils;

import com.example.agriecommerce.entity.OrderStatus;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface OrderInfoDto {
    Long getId();
    LocalDateTime getDateCreated();
    @Value(value = "#{@orderStatusConverter.convertToEntityAttribute(target.orderStatus)}")
    OrderStatus getOrderStatus();
    Long getAddressId();
    Long getUserId();
    Long getProductId();
    String getProductName();
    String getProductImage();
    Long getStandardPrice();
    Long getDiscountPrice();
    String getWarehouseName();
    Long getQuantity();
    String getOrderNumber();
}
