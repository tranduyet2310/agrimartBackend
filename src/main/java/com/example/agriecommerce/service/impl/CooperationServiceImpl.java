package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.*;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.repository.*;
import com.example.agriecommerce.service.CooperationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CooperationServiceImpl implements CooperationService {
    private final CooperationRepository cooperationRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final ModelMapper modelMapper;
    private final FieldRepository fieldRepository;

    @Autowired
    public CooperationServiceImpl(CooperationRepository cooperationRepository,
                                  SupplierRepository supplierRepository,
                                  UserRepository userRepository,
                                  ModelMapper modelMapper,
                                  FieldRepository fieldRepository,
                                  UserAddressRepository userAddressRepository) {
        this.cooperationRepository = cooperationRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.fieldRepository = fieldRepository;
        this.userAddressRepository = userAddressRepository;
    }

    @Override
    public CooperationDto createCooperation(Long supplierId, CooperationDto cooperationDto) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier does not exists")
        );

        Long userId = cooperationDto.getUserId();
        Long fieldId = cooperationDto.getFieldId();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user does not exists")
        );
        Field field = fieldRepository.findById(fieldId).orElseThrow(
                () -> new ResourceNotFoundException("Field does not exists")
        );

        Cooperation cooperation = new Cooperation();
        cooperation.setSupplier(supplier);
        cooperation.setUser(user);
        cooperation.setField(field);
        cooperation.setFullName(cooperationDto.getFullName());
        cooperation.setDescription(cooperationDto.getDescription());
        cooperation.setCropsName(cooperationDto.getCropsName());
        cooperation.setRequireYield(cooperationDto.getRequireYield());
        cooperation.setInvestment(cooperationDto.getInvestment());
        cooperation.setContact(cooperationDto.getContact());
        cooperation.setCooperationStatus(cooperationDto.getCooperationStatus());

        Cooperation savedCooperation = cooperationRepository.save(cooperation);

        return modelMapper.map(savedCooperation, CooperationDto.class);
    }

    @Override
    public CooperationDto updateCooperation(Long cooperationId, CooperationDto cooperationDto) {
        Cooperation cooperation = cooperationRepository.findById(cooperationId).orElseThrow(
                () -> new ResourceNotFoundException("cooperation does not exists")
        );

        cooperation.setId(cooperationId);
        cooperation.setFullName(cooperationDto.getFullName());
        cooperation.setDescription(cooperationDto.getDescription());
        cooperation.setRequireYield(cooperationDto.getRequireYield());
        cooperation.setInvestment(cooperationDto.getInvestment());
        cooperation.setContact(cooperationDto.getContact());

        Cooperation updatedCooperation = cooperationRepository.save(cooperation);

        return modelMapper.map(updatedCooperation, CooperationDto.class);
    }

    @Override
    public CooperationDto getCooperationById(Long cooperationId) {
        Cooperation cooperation = cooperationRepository.findById(cooperationId).orElseThrow(
                () -> new ResourceNotFoundException("Cooperation does not exists")
        );
        return modelMapper.map(cooperation, CooperationDto.class);
    }

    @Override
    public List<CooperationDto> getCooperationBySupplierId(Long supplierId) {
        List<Cooperation> cooperations = cooperationRepository.findBySupplierId(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier does not have any cooperation")
        );
        return cooperations.stream()
                .map(cooperation -> modelMapper.map(cooperation, CooperationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CooperationResponse getCooperationBySupplierIdV2(Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Cooperation> cooperationPage = cooperationRepository.findBySupplierId(supplierId, pageable).orElseThrow(
                () -> new ResourceNotFoundException("Supplier does not have any cooperation")
        );

        List<Cooperation> cooperationList = cooperationPage.getContent();
        List<CooperationDto> content = cooperationList.stream().map(cooperation -> modelMapper.map(cooperation, CooperationDto.class)).toList();

        CooperationResponse cooperationResponse = new CooperationResponse();
        cooperationResponse.setContent(content);
        cooperationResponse.setPageNo(cooperationPage.getNumber());
        cooperationResponse.setPageSize(cooperationPage.getSize());
        cooperationResponse.setTotalElements(cooperationPage.getTotalElements());
        cooperationResponse.setTotalPage(cooperationPage.getTotalPages());
        cooperationResponse.setLast(cooperationPage.isLast());

        return cooperationResponse;
    }

    @Override
    public CooperationResponse getCooperationSortByField(Long supplierId, Long fieldId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Cooperation> cooperationPage = cooperationRepository.findBySupplierIdAndFieldId(supplierId, fieldId, pageable).orElseThrow(
                () -> new ResourceNotFoundException("Supplier does not have any cooperation")
        );

        List<Cooperation> cooperationList = cooperationPage.getContent();
        List<CooperationDto> content = cooperationList.stream().map(cooperation -> modelMapper.map(cooperation, CooperationDto.class)).toList();

        CooperationResponse cooperationResponse = new CooperationResponse();
        cooperationResponse.setContent(content);
        cooperationResponse.setPageNo(cooperationPage.getNumber());
        cooperationResponse.setPageSize(cooperationPage.getSize());
        cooperationResponse.setTotalElements(cooperationPage.getTotalElements());
        cooperationResponse.setTotalPage(cooperationPage.getTotalPages());
        cooperationResponse.setLast(cooperationPage.isLast());

        return cooperationResponse;
    }

    @Override
    public List<CooperationDto> getCooperationByUserId(Long userId) {
        List<Cooperation> cooperations = cooperationRepository.findByUserId(userId).orElseThrow(
                () -> new ResourceNotFoundException("user does not have any cooperation")
        );
        return cooperations.stream()
                .map(cooperation -> modelMapper.map(cooperation, CooperationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResultDto calculateCurrentTotal(Long fieldId, Long supplierId) {
        Double result = cooperationRepository.calculateYieldAccepted(fieldId, supplierId);
        if (result == null) {
            result = 0.0;
        }
        return new ResultDto(true, result.toString());
    }

    @Override
    public CooperationDto updateStatus(Long cooperationId, CooperationDto cooperationDto) {
        Cooperation cooperation = cooperationRepository.findById(cooperationId).orElseThrow(
                () -> new ResourceNotFoundException("cooperation does not exists")
        );
        cooperation.setId(cooperationId);
        cooperation.setCooperationStatus(cooperationDto.getCooperationStatus());

        Cooperation updatedCooperation = cooperationRepository.save(cooperation);
        return modelMapper.map(updatedCooperation, CooperationDto.class);
    }

    @Override
    public CooperationDto updateAddress(Long cooperationId, Long addressId) {
        Cooperation cooperation = cooperationRepository.findById(cooperationId).orElseThrow(
                () -> new ResourceNotFoundException("cooperation does not exists")
        );
        UserAddress address = userAddressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("address does not exists")
        );

        cooperation.setAddress(address);

        Cooperation updatedCooperation = cooperationRepository.save(cooperation);

        return modelMapper.map(updatedCooperation, CooperationDto.class);
    }

    @Override
    public CooperationDto updatePayment(Long cooperationId, CooperationDto cooperationDto) {
        Cooperation cooperation = cooperationRepository.findById(cooperationId).orElseThrow(
                () -> new ResourceNotFoundException("cooperation does not exists")
        );
        cooperation.setPaymentStatus(cooperationDto.getPaymentStatus());
        Cooperation updatedCooperation = cooperationRepository.save(cooperation);
        return modelMapper.map(updatedCooperation, CooperationDto.class);
    }

}
