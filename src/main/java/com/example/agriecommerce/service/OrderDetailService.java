package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.OrderDetailDto;

import java.util.List;

public interface OrderDetailService {
    OrderDetailDto createOrderDetail(Long orderId, OrderDetailDto dto);
    List<OrderDetailDto> getOrderDetailByOrderId(Long orderId);
}
