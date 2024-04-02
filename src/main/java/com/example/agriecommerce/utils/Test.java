package com.example.agriecommerce.utils;

import com.example.agriecommerce.entity.OrderStatus;

import java.math.BigDecimal;
import java.math.MathContext;

import static com.example.agriecommerce.utils.OrderNumberGenerator.generateOrderNumber;

public class Test {

    public static void main(String[] args) {
        String orderNumber = OrderStatus.PROCESSING.name();
        System.out.println("Generated Order Number: " + orderNumber);
        try {
            OrderStatus value = OrderStatus.valueOf("heelo");
            System.out.println("order status enum: " + value);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
