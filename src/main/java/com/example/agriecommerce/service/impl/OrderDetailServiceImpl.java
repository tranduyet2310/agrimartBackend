package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Order;
import com.example.agriecommerce.entity.OrderDetail;
import com.example.agriecommerce.entity.Product;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.OrderDetailDto;
import com.example.agriecommerce.repository.OrderDetailRepository;
import com.example.agriecommerce.repository.OrderRepository;
import com.example.agriecommerce.repository.ProductRepository;
import com.example.agriecommerce.service.OrderDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public OrderDetailServiceImpl(OrderRepository orderRepository,
                                  OrderDetailRepository orderDetailRepository,
                                  ProductRepository productRepository,
                                  ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDetailDto createOrderDetail(Long orderId, OrderDetailDto dto) {
        Long productId = dto.getProductId();

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("order does not exists")
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("product does not exists")
        );

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setQuantity(dto.getQuantity());

        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);

        return modelMapper.map(savedOrderDetail, OrderDetailDto.class);
    }

    @Override
    public List<OrderDetailDto> getOrderDetailByOrderId(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId).orElseThrow(
                () -> new ResourceNotFoundException("order does not have any order detail")
        );
        return orderDetails.stream()
                .map(orderDetail -> modelMapper.map(orderDetail, OrderDetailDto.class))
                .collect(Collectors.toList());
    }
}
