package com.example.agriecommerce.controller;

import com.example.agriecommerce.entity.OrderDetail;
import com.example.agriecommerce.payload.OrderDetailDto;
import com.example.agriecommerce.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    @Autowired
    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @PostMapping("{orderId}")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<OrderDetailDto> createOrderDetail(@PathVariable("orderId") Long orderId,
                                                            @RequestBody OrderDetailDto dto){
        OrderDetailDto orderDetailDto = orderDetailService.createOrderDetail(orderId, dto);
        return new ResponseEntity<>(orderDetailDto, HttpStatus.CREATED);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<List<OrderDetailDto>> getDetailsByOrderId(@PathVariable("orderId") Long orderId){
        return ResponseEntity.ok(orderDetailService.getOrderDetailByOrderId(orderId));
    }
}
