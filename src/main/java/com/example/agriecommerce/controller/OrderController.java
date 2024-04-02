package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.OrderDto;
import com.example.agriecommerce.service.OrderService;
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
    public ResponseEntity<List<OrderDto>> getOrderByUserId(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(orderService.getOrderByUserId(userId));
    }
}
