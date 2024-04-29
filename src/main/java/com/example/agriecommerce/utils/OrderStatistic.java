package com.example.agriecommerce.utils;

import com.example.agriecommerce.entity.OrderStatus;
import org.springframework.beans.factory.annotation.Value;

public interface OrderStatistic {
    Long getId();
    @Value(value = "#{@orderStatusConverter.convertToEntityAttribute(target.orderStatus)}")
    OrderStatus getOrderStatus();
    Long getTotal();
    Long getQuantity();
    String getProductName();
    Long getStandardPrice();
    Long getDiscountPrice();
    String getProductImage();
    String getFullName();
}
