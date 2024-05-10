package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.service.CooperativePaymentService;
import com.example.agriecommerce.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cooperative")
public class CooperativePaymentController {
    private final CooperativePaymentService cooperativePaymentService;
    @Autowired
    public CooperativePaymentController(CooperativePaymentService cooperativePaymentService) {
        this.cooperativePaymentService = cooperativePaymentService;
    }

    @PostMapping("{userId}")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<CooperativePaymentDto> createOrder(@PathVariable("userId") Long userId,
                                                @RequestBody CooperativePaymentDto dto) {
        return new ResponseEntity<>(cooperativePaymentService.createOrder(userId, dto), HttpStatus.CREATED);
    }

    @GetMapping("{id}/info")
    public ResponseEntity<CooperativePaymentDto> getOrderById(@PathVariable("id") Long cooperativePaymentId) {
        return ResponseEntity.ok(cooperativePaymentService.getOrderById(cooperativePaymentId));
    }

    @GetMapping("{userId}")
    public ResponseEntity<CooperativePaymentResponse> getOrderByUserId(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return ResponseEntity.ok(cooperativePaymentService.getOrderByUserId(userId, pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("{supplierId}/list")
    public ResponseEntity<CooperativePaymentResponse> getOrderBySupplierId(
            @PathVariable("supplierId") Long supplierId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return ResponseEntity.ok(cooperativePaymentService.getOrderBySupplierId(supplierId, pageNo, pageSize, sortBy, sortDir));
    }
}
