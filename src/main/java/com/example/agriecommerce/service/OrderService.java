package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.OrderDto;
import com.example.agriecommerce.payload.OrderResponse;
import com.example.agriecommerce.payload.ResultDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(Long userId, OrderDto orderDto);
    OrderDto updateOrderStatus(Long orderId, String orderStatus);
    OrderDto updateOrder(Long orderId, OrderDto orderDto);
    OrderResponse getOrderByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
    ResultDto deleteOrder(Long orderId);
    ResultDto hasUserPurchasedProduct(Long userId, Long productId);
}
