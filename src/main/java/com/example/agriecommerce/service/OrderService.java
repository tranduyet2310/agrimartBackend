package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.*;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(Long userId, OrderDto orderDto);
    OrderDto updateOrderStatus(Long orderId, String orderStatus);
    OrderBasicInfoDto updateOrderStatusV2(Long orderId, String orderStatus);
    OrderBasicInfoDto getOrderById(Long orderId);
    List<OrderStatisticDto> getOrderStatistic(Long supplierId, String datePattern);
    List<OrderStatisticDto> getRecentOrderStatistic(Long supplierId, String datePattern);
    OrderStatisticDto getStatistic(Long supplierId, String datePattern);
    OrderDto updateOrder(Long orderId, OrderDto orderDto);
    OrderResponse getOrderByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
    OrderInfoResponse getOrderBySupplierId(Long supplierId, String datePattern, int pageNo, int pageSize, String sortBy, String sortDir);
    OrderResponse getAllOrders(int pageNo, int pageSize, String sortBy, String sortDir);
    ResultDto deleteOrder(Long orderId);
    ResultDto hasUserPurchasedProduct(Long userId, Long productId);
    ComparationDto getStatisticOrder(int month, int year);
    ComparationDto getStatisticRevenue(int month, int year);
    List<BarChartOrderDto> getChartData(int month, int year);
    List<PieChartDto> getPieChartData(int month, int year);
}
