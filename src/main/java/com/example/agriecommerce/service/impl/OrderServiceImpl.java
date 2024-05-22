package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Order;
import com.example.agriecommerce.entity.OrderStatus;
import com.example.agriecommerce.entity.User;
import com.example.agriecommerce.entity.UserAddress;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.repository.CooperativePaymentRepository;
import com.example.agriecommerce.repository.OrderRepository;
import com.example.agriecommerce.repository.UserAddressRepository;
import com.example.agriecommerce.repository.UserRepository;
import com.example.agriecommerce.service.OrderService;
import com.example.agriecommerce.utils.AppConstants;
import com.example.agriecommerce.utils.OrderInfoDto;
import com.example.agriecommerce.utils.OrderNumberGenerator;
import com.example.agriecommerce.utils.OrderStatistic;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final CooperativePaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            UserAddressRepository userAddressRepository,
                            ModelMapper modelMapper,
                            CooperativePaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
        this.modelMapper = modelMapper;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public OrderDto createOrder(Long userId, OrderDto orderDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user does not exists")
        );

        Long addressId = orderDto.getAddressId();
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
        Long addressId = orderDto.getAddressId();

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
    public OrderBasicInfoDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order does not exists")
        );
        return modelMapper.map(order, OrderBasicInfoDto.class);
    }

    @Override
    public OrderResponse getOrderByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Order> orderPage = orderRepository.findByUserId(userId, pageable).orElseThrow(
                () -> new ResourceNotFoundException("user's order is empty")
        );

        List<Order> orders = orderPage.getContent();
        List<OrderDto> content = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(content);
        orderResponse.setPageNo(orderPage.getNumber());
        orderResponse.setPageSize(orderPage.getSize());
        orderResponse.setTotalElements(orderPage.getTotalElements());
        orderResponse.setTotalPage(orderPage.getTotalPages());
        orderResponse.setLast(orderPage.isLast());

        return orderResponse;
    }

    @Override
    public OrderResponse getAllOrders(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<Order> orders = orderPage.getContent();
        List<OrderDto> content = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(content);
        orderResponse.setPageNo(orderPage.getNumber());
        orderResponse.setPageSize(orderPage.getSize());
        orderResponse.setTotalElements(orderPage.getTotalElements());
        orderResponse.setTotalPage(orderPage.getTotalPages());
        orderResponse.setLast(orderPage.isLast());

        return orderResponse;
    }

    @Override
    public OrderInfoResponse getOrderBySupplierId(Long supplierId, String datePattern, int pageNo, int pageSize, String sortBy, String sortDir) {
        String formatDate = "%"+datePattern+"%";

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<OrderInfoDto> orderPage = orderRepository.findOrderBySupplier(supplierId, formatDate, pageable).orElseThrow(
                () -> new ResourceNotFoundException("supplier's order is empty")
        );

        List<OrderInfoDto> pageContent = orderPage.getContent();
        List<OrderInfo> orderInfos = new ArrayList<>();

        Map<Long, List<OrderProductDto>> groupProduct = new HashMap<>();
        for (OrderInfoDto ofd : pageContent) {
            if (!groupProduct.containsKey(ofd.getId())) {
                groupProduct.put(ofd.getId(), new ArrayList<>());

                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setId(ofd.getId());
                orderInfo.setDateCreated(ofd.getDateCreated());
                orderInfo.setOrderStatus(ofd.getOrderStatus());
                orderInfo.setAddressId(ofd.getAddressId());
                orderInfo.setUserId(ofd.getUserId());
                orderInfo.setOrderNumber(ofd.getOrderNumber());

                orderInfos.add(orderInfo);
            }

            OrderProductDto productDto = new OrderProductDto();
            productDto.setProductId(ofd.getProductId());
            productDto.setProductName(ofd.getProductName());
            productDto.setProductImage(ofd.getProductImage());
            productDto.setStandardPrice(ofd.getStandardPrice());
            productDto.setDiscountPrice(ofd.getDiscountPrice());
            productDto.setWarehouseName(ofd.getWarehouseName());
            productDto.setQuantity(ofd.getQuantity());

            groupProduct.get(ofd.getId()).add(productDto);
        }

        for (long orderId : groupProduct.keySet()) {
            for (OrderInfo oi : orderInfos) {
                if (oi.getId() == orderId) {
                    oi.setProductList(groupProduct.get(orderId));
                }
            }
        }

        OrderInfoResponse orderInfoResponse = new OrderInfoResponse();
        orderInfoResponse.setContent(orderInfos);
        orderInfoResponse.setPageNo(orderPage.getNumber());
        orderInfoResponse.setPageSize(orderPage.getSize());
        orderInfoResponse.setTotalElements(orderPage.getTotalElements());
        orderInfoResponse.setTotalPage(orderPage.getTotalPages());
        orderInfoResponse.setLast(orderPage.isLast());

        return orderInfoResponse;
    }

    @Override
    public List<OrderStatisticDto> getOrderStatistic(Long supplierId, String datePattern) {
        String formatDate = "%"+datePattern+"%";
        List<OrderStatistic> orderStatistics = orderRepository.getOrderStatistic(supplierId, formatDate).orElseThrow(
                () -> new ResourceNotFoundException("supplier's order is empty")
        );

        List<OrderStatisticDto> list = new ArrayList<>();
        for (OrderStatistic os : orderStatistics){
            OrderStatisticDto dto = new OrderStatisticDto();
            dto.setId(os.getId());
            dto.setQuantity(os.getQuantity());
            dto.setOrderStatus(os.getOrderStatus());
            dto.setProductName(os.getProductName());
            dto.setDiscountPrice(os.getDiscountPrice());
            dto.setStandardPrice(os.getStandardPrice());
            dto.setProductImage(os.getProductImage());
            dto.setTotal(os.getTotal());
            dto.setUserFullName(os.getFullName());

            list.add(dto);
        }

        return list;
    }

    public List<OrderStatisticDto> getRecentOrderStatistic(Long supplierId, String datePattern) {
        String formatDate = "%"+datePattern+"%";
        List<OrderStatistic> orderStatistics = orderRepository.getRecentOrderStatistic(supplierId, formatDate).orElseThrow(
                () -> new ResourceNotFoundException("supplier's order is empty")
        );

        List<OrderStatisticDto> list = new ArrayList<>();
        for (OrderStatistic os : orderStatistics){
            OrderStatisticDto dto = new OrderStatisticDto();
            dto.setId(os.getId());
            dto.setQuantity(os.getQuantity());
            dto.setOrderStatus(os.getOrderStatus());
            dto.setProductName(os.getProductName());
            dto.setDiscountPrice(os.getDiscountPrice());
            dto.setStandardPrice(os.getStandardPrice());
            dto.setProductImage(os.getProductImage());
            dto.setTotal(os.getTotal());
            dto.setUserFullName(os.getFullName());

            list.add(dto);
        }

        return list;
    }

    @Override
    public OrderStatisticDto getStatistic(Long supplierId, String datePattern) {
        String formatDate = "%"+datePattern+"%";
        OrderStatistic orderStatistics = orderRepository.getStatistic(supplierId, formatDate).orElseThrow(
                () -> new ResourceNotFoundException("supplier's order is empty")
        );

        OrderStatisticDto dto = new OrderStatisticDto();
        dto.setQuantity(orderStatistics.getQuantity());
        dto.setTotal(orderStatistics.getTotal());

        return dto;
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
        } catch (Exception e) {
            throw new ResourceNotFoundException(orderStatus + " does not exists");
        }

        order.setId(orderId);
        order.setOrderStatus(status);

        Order updatedOrder = orderRepository.save(order);

        return modelMapper.map(updatedOrder, OrderDto.class);
    }

    @Override
    public OrderBasicInfoDto updateOrderStatusV2(Long orderId, String orderStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("order does not exists")
        );
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(orderStatus);
        } catch (Exception e) {
            throw new ResourceNotFoundException(orderStatus + " does not exists");
        }

        order.setId(orderId);
        order.setOrderStatus(status);

        Order updatedOrder = orderRepository.save(order);

        return modelMapper.map(updatedOrder, OrderBasicInfoDto.class);
    }

    @Override
    public ResultDto hasUserPurchasedProduct(Long userId, Long productId) {
        int result = orderRepository.hasUserPurchasedProduct(userId, productId);
        ResultDto dto = new ResultDto();
        dto.setSuccessful(result > 0);
        dto.setMessage(result + "");
        return dto;
    }

    @Override
    public ComparationDto getStatisticOrder(int month, int year) {
        ComparationDto dto = new ComparationDto();
        int previousMonth = month - 1;
        int previousYear = year;
        if (previousMonth == 0){
            previousMonth = 12;
            previousYear = year - 1;
        }
        double current = orderRepository.countTotalOrderByMonthAndYear(month, year);
        double previous = orderRepository.countTotalOrderByMonthAndYear(previousMonth, previousYear);
        double gaps = current - previous;

        dto.setCurrent(current);
        dto.setPrevious(previous);
        dto.setGaps(gaps);

        return dto;
    }

    @Override
    public ComparationDto getStatisticRevenue(int month, int year) {
        ComparationDto dto = new ComparationDto();
        int previousMonth = month - 1;
        int previousYear = year;
        if (previousMonth == 0){
            previousMonth = 12;
            previousYear = year - 1;
        }
        double currentOrder = orderRepository.calculateTotalRevenue(month, year);
        double previousOrder = orderRepository.calculateTotalRevenue(previousMonth, previousYear);
        double currentCooperation = paymentRepository.calculateTotalRevenue(month, year);
        double previousCooperation = paymentRepository.calculateTotalRevenue(previousMonth, previousYear);

        double current = currentOrder+currentCooperation;
        double previous = previousOrder+previousCooperation;
        double gaps = current - previous;

        dto.setCurrent(current);
        dto.setPrevious(previous);
        dto.setGaps(gaps);

        return dto;
    }

    @Override
    public List<BarChartOrderDto> getChartData(int month, int year) {
        List<BarChartOrderDto> dataSource = new ArrayList<>();
        if (month <= 0 || month > 12)
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Month is invalid, m=" + month);

        for (int i = 1; i <= 12; i++) {
            BarChartOrderDto item = new BarChartOrderDto();
            String name = AppConstants.listMonths[i - 1];

            double currentRevenueOrder = orderRepository.calculateTotalRevenue(i, year);
            double currentRevenueCooperation = paymentRepository.calculateTotalRevenue(i, year);
            double currentOrder = orderRepository.countTotalOrderByMonthAndYear(i, year);
            double currentCooperation = paymentRepository.countTotalOrder(i, year);

            item.setName(name);
            item.setOrder(currentOrder + currentCooperation);
            item.setRevenue(currentRevenueOrder + currentRevenueCooperation);

            dataSource.add(item);
        }

        return dataSource;
    }

    @Override
    public List<PieChartDto> getPieChartData(int month, int year) {
        List<PieChartDto> dataSource = new ArrayList<>();
        if (month <= 0 || month > 12)
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Month is invalid, m=" + month);

        for (int i=0; i<=4; i++){
            PieChartDto item = new PieChartDto();
            String name = AppConstants.listOrderStatus[i];
            long value = orderRepository.statisticOrderStatus(month, year, i);
            item.setName(name);
            item.setValue(value);
            dataSource.add(item);
        }

        return dataSource;
    }
}
