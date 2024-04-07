package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.OrderDto;
import com.example.agriecommerce.payload.OrderResponse;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.service.OrderService;
import com.example.agriecommerce.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("{userId}")
    public ResponseEntity<OrderDto> createOrder(@PathVariable("userId") Long userId,
                                                @RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.createOrder(userId, orderDto), HttpStatus.CREATED);
    }

    @GetMapping("{userId}")
    public ResponseEntity<OrderResponse> getOrderByUserId(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return ResponseEntity.ok(orderService.getOrderByUserId(userId, pageNo, pageSize, sortBy, sortDir));
    }

    @PatchMapping("{orderId}")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable("orderId") Long orderId,
                                                      @RequestParam("orderStatus") String orderStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, orderStatus));
    }

    @GetMapping("{userId}/{productId}")
    public ResponseEntity<ResultDto> checkUserPurchasedOrNot(@PathVariable("userId") Long userId,
                                                             @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(orderService.hasUserPurchasedProduct(userId, productId));
    }
}
