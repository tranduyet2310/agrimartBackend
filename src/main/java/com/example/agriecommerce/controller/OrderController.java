package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.service.OrderService;
import com.example.agriecommerce.utils.AppConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "REST APIs for Order")
@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("{userId}")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<OrderDto> createOrder(@PathVariable("userId") Long userId,
                                                @RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.createOrder(userId, orderDto), HttpStatus.CREATED);
    }

    @GetMapping("{orderId}/info")
    public ResponseEntity<OrderBasicInfoDto> getOrderById(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return ResponseEntity.ok(orderService.getAllOrders(pageNo, pageSize, sortBy, sortDir));
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

    @GetMapping("{supplierId}/list")
    public ResponseEntity<OrderInfoResponse> getOrderBySupplierId(
            @PathVariable("supplierId") Long supplierId,
            @RequestParam(value = "date") String datePattern,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return ResponseEntity.ok(orderService.getOrderBySupplierId(supplierId, datePattern, pageNo, pageSize, sortBy, sortDir));
    }

    @PatchMapping("{orderId}")
    @PreAuthorize(("hasRole('USER') or hasRole('ADMIN')"))
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable("orderId") Long orderId,
                                                      @RequestParam("orderStatus") String orderStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, orderStatus));
    }

    @PatchMapping("{orderId}/status")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<OrderBasicInfoDto> updateOrderStatusV2(@PathVariable("orderId") Long orderId,
                                                                 @RequestParam("orderStatus") String orderStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatusV2(orderId, orderStatus));
    }

    @GetMapping("{userId}/{productId}")
    public ResponseEntity<ResultDto> checkUserPurchasedOrNot(@PathVariable("userId") Long userId,
                                                             @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(orderService.hasUserPurchasedProduct(userId, productId));
    }

    @GetMapping("{supplierId}/statistic")
    public ResponseEntity<List<OrderStatisticDto>> getOrderStatistic(@PathVariable("supplierId") Long userId,
                                                                     @RequestParam(value = "date") String datePattern) {
        return ResponseEntity.ok(orderService.getOrderStatistic(userId, datePattern));
    }

    @GetMapping("{supplierId}/recent")
    public ResponseEntity<List<OrderStatisticDto>> getRecentOrderStatistic(@PathVariable("supplierId") Long userId,
                                                                           @RequestParam(value = "date") String datePattern) {
        return ResponseEntity.ok(orderService.getRecentOrderStatistic(userId, datePattern));
    }

    @GetMapping("{supplierId}/total")
    public ResponseEntity<OrderStatisticDto> getStatistic(@PathVariable("supplierId") Long userId,
                                                          @RequestParam(value = "date") String datePattern) {
        return ResponseEntity.ok(orderService.getStatistic(userId, datePattern));
    }

    @GetMapping("statistic")
    public ResponseEntity<ComparationDto> getStatisticOrder(@RequestParam("m") int month,
                                                            @RequestParam("y") int year) {
        return ResponseEntity.ok(orderService.getStatisticOrder(month, year));
    }

    @GetMapping("revenue")
    public ResponseEntity<ComparationDto> getStatisticRevenue(@RequestParam("m") int month,
                                                              @RequestParam("y") int year) {
        return ResponseEntity.ok(orderService.getStatisticRevenue(month, year));
    }

    @GetMapping("chart")
    public ResponseEntity<List<BarChartOrderDto>> getChartData(@RequestParam("m") int month,
                                                               @RequestParam("y") int year) {
        return ResponseEntity.ok(orderService.getChartData(month, year));
    }

    @GetMapping("pie")
    public ResponseEntity<List<PieChartDto>> getPieChartData(@RequestParam("m") int month,
                                                             @RequestParam("y") int year) {
        return ResponseEntity.ok(orderService.getPieChartData(month, year));
    }
}
