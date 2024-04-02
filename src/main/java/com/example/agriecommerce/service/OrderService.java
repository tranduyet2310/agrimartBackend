package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.OrderDto;
import com.example.agriecommerce.payload.ResultDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(Long userId, OrderDto orderDto);
    OrderDto updateOrderStatus(Long orderId, String orderStatus);
    OrderDto updateOrder(Long orderId, OrderDto orderDto);
    List<OrderDto> getOrderByUserId(Long userId);
    ResultDto deleteOrder(Long orderId);
}
