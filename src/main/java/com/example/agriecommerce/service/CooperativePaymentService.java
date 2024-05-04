package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.*;

public interface CooperativePaymentService {
    CooperativePaymentDto createOrder(Long userId, CooperativePaymentDto paymentDto);
    CooperativePaymentDto getOrderById(Long cooperativePaymentId);
    CooperativePaymentResponse getOrderByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir);
    CooperativePaymentResponse getOrderBySupplierId(Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir);
}
