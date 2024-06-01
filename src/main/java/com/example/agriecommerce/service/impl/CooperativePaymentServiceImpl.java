package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Cooperation;
import com.example.agriecommerce.entity.CooperativePayment;
import com.example.agriecommerce.entity.Supplier;
import com.example.agriecommerce.entity.User;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.CooperativePaymentDto;
import com.example.agriecommerce.payload.CooperativePaymentResponse;
import com.example.agriecommerce.repository.CooperationRepository;
import com.example.agriecommerce.repository.CooperativePaymentRepository;
import com.example.agriecommerce.repository.SupplierRepository;
import com.example.agriecommerce.repository.UserRepository;
import com.example.agriecommerce.service.CooperativePaymentService;
import com.example.agriecommerce.utils.OrderNumberGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CooperativePaymentServiceImpl implements CooperativePaymentService {
    private final CooperationRepository cooperationRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CooperativePaymentRepository paymentRepository;
    @Autowired
    public CooperativePaymentServiceImpl(CooperationRepository cooperationRepository,
                                         SupplierRepository supplierRepository,
                                         UserRepository userRepository,
                                         ModelMapper modelMapper,
                                         CooperativePaymentRepository paymentRepository) {
        this.cooperationRepository = cooperationRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public CooperativePaymentDto createOrder(Long userId, CooperativePaymentDto paymentDto) {
        Long supplierId = paymentDto.getSupplierId();
        Long cooperationId = paymentDto.getCooperationId();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user does not exists")
        );
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier does not exists")
        );
        Cooperation cooperation = cooperationRepository.findById(cooperationId).orElseThrow(
                () -> new ResourceNotFoundException("cooperation does not exists")
        );

        String orderNumber = OrderNumberGenerator.generateOrderNumber();

        CooperativePayment cooperativePayment = new CooperativePayment();
        cooperativePayment.setUser(user);
        cooperativePayment.setCooperation(cooperation);
        cooperativePayment.setSupplier(supplier);
        cooperativePayment.setTotal(paymentDto.getTotal());
        cooperativePayment.setPaymentMethod(paymentDto.getPaymentMethod());
        cooperativePayment.setPaymentStatus(paymentDto.getPaymentStatus());
        cooperativePayment.setOrderStatus(paymentDto.getOrderStatus());
        cooperativePayment.setOrderNumber(orderNumber);

        CooperativePayment savedEntity = paymentRepository.save(cooperativePayment);

        return modelMapper.map(savedEntity, CooperativePaymentDto.class);
    }

    @Override
    public CooperativePaymentDto getOrderById(Long cooperativePaymentId) {
        CooperativePayment payment = paymentRepository.findById(cooperativePaymentId).orElseThrow(
                () -> new ResourceNotFoundException("CooperativePayment does not exists")
        );
        return modelMapper.map(payment, CooperativePaymentDto.class);
    }

    @Override
    public CooperativePaymentResponse getOrderByUserId(Long userId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<CooperativePayment> orderPage = paymentRepository.findByUserId(userId, pageable).orElseThrow(
                () -> new ResourceNotFoundException("user's cooperative payment is empty")
        );

        List<CooperativePayment> orders = orderPage.getContent();
        List<CooperativePaymentDto> content = orders.stream().map(order -> modelMapper.map(order, CooperativePaymentDto.class)).collect(Collectors.toList());

        CooperativePaymentResponse paymentResponse = new CooperativePaymentResponse();
        paymentResponse.setContent(content);
        paymentResponse.setPageNo(orderPage.getNumber());
        paymentResponse.setPageSize(orderPage.getSize());
        paymentResponse.setTotalElements(orderPage.getTotalElements());
        paymentResponse.setTotalPage(orderPage.getTotalPages());
        paymentResponse.setLast(orderPage.isLast());

        return paymentResponse;
    }

    @Override
    public CooperativePaymentResponse getOrderBySupplierId(Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<CooperativePayment> orderPage = paymentRepository.findBySupplierId(supplierId, pageable).orElseThrow(
                () -> new ResourceNotFoundException("supplier's cooperative payment is empty")
        );

        List<CooperativePayment> orders = orderPage.getContent();
        List<CooperativePaymentDto> content = orders.stream().map(order -> modelMapper.map(order, CooperativePaymentDto.class)).collect(Collectors.toList());

        CooperativePaymentResponse paymentResponse = new CooperativePaymentResponse();
        paymentResponse.setContent(content);
        paymentResponse.setPageNo(orderPage.getNumber());
        paymentResponse.setPageSize(orderPage.getSize());
        paymentResponse.setTotalElements(orderPage.getTotalElements());
        paymentResponse.setTotalPage(orderPage.getTotalPages());
        paymentResponse.setLast(orderPage.isLast());

        return paymentResponse;
    }
}
