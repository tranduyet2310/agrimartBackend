package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Order;
import com.example.agriecommerce.entity.OrderStatus;
import com.example.agriecommerce.entity.User;
import com.example.agriecommerce.entity.UserAddress;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.OrderDto;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.repository.OrderRepository;
import com.example.agriecommerce.repository.ProductRepository;
import com.example.agriecommerce.repository.UserAddressRepository;
import com.example.agriecommerce.repository.UserRepository;
import com.example.agriecommerce.service.OrderService;
import com.example.agriecommerce.utils.OrderNumberGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            UserAddressRepository userAddressRepository,
                            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDto createOrder(Long userId, OrderDto orderDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user does not exists")
        );

        Long addressId = orderDto.getUserAddressId();
        UserAddress address = userAddressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("address does not exists")
        );

        String orderNumber = OrderNumberGenerator.generateOrderNumber();

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setTotal(orderDto.getTotal());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setOrderNumber(orderNumber);

        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public OrderDto updateOrder(Long orderId, OrderDto orderDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("order does not exists")
        );

        Long userId = orderDto.getUserId();
        Long addressId = orderDto.getUserAddressId();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user does not exists")
        );
        UserAddress address = userAddressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("address does not exists")
        );

        order.setId(orderId);
        order.setUser(user);
        order.setAddress(address);
        order.setTotal(orderDto.getTotal());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setOrderStatus(OrderStatus.PROCESSING);

        Order updatedOrder = orderRepository.save(order);

        return modelMapper.map(updatedOrder, OrderDto.class);
    }

    @Override
    public List<OrderDto> getOrderByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId).orElseThrow(
                () -> new ResourceNotFoundException("user's order is empty")
        );
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResultDto deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("order does not exists")
        );
        orderRepository.delete(order);
        return new ResultDto(true, "Delete order successfully");
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, String orderStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("order does not exists")
        );
        OrderStatus status;
        try {
             status = OrderStatus.valueOf(orderStatus);
        }catch (Exception e){
            throw new ResourceNotFoundException(orderStatus+" does not exists");
        }

        order.setId(orderId);
        order.setOrderStatus(status);

        Order updatedOrder = orderRepository.save(order);

        return modelMapper.map(updatedOrder, OrderDto.class);
    }
}
