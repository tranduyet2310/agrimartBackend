package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Field;
import com.example.agriecommerce.entity.FieldDetail;
import com.example.agriecommerce.entity.Supplier;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.FieldDto;
import com.example.agriecommerce.repository.FieldRepository;
import com.example.agriecommerce.repository.SupplierRepository;
import com.example.agriecommerce.service.FieldService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    @Autowired
    public FieldServiceImpl(FieldRepository fieldRepository, SupplierRepository supplierRepository, ModelMapper modelMapper) {
        this.fieldRepository = fieldRepository;
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public FieldDto createNewCrops(Long supplierId, FieldDto fieldDto) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier does not exists")
        );

        Field field = new Field();
        List<FieldDetail> fieldDetails = new ArrayList<>();

        field.setCropsName(fieldDto.getCropsName());
        field.setArea(fieldDto.getArea());
        field.setCropsType(fieldDto.getCropsType());
        field.setSeason(fieldDto.getSeason());
        field.setEstimateYield(fieldDto.getEstimateYield());
        field.setFieldDetails(fieldDetails);
        field.setSupplier(supplier);

        Field savedField = fieldRepository.save(field);

        return modelMapper.map(savedField, FieldDto.class);
    }

    @Override
    public FieldDto updateCropsField(Long fieldId, FieldDto fieldDto) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(
                () -> new ResourceNotFoundException("field does not exists")
        );
        Supplier supplier = supplierRepository.findById(fieldDto.getSupplierId()).orElseThrow(
                () -> new ResourceNotFoundException("supplier does not exists")
        );

        field.setId(fieldId);
        field.setCropsName(fieldDto.getCropsName());
        field.setArea(fieldDto.getArea());
        field.setCropsType(fieldDto.getCropsType());
        field.setSeason(fieldDto.getSeason());
        field.setEstimateYield(fieldDto.getEstimateYield());
        field.setSupplier(supplier);

//        List<FieldDetail> fieldDetails = field.getFieldDetails();
//
//        field.setFieldDetails(fieldDetails);

        return null;
    }

    @Override
    public List<FieldDto> getAllFieldBySupplierId(Long supplierId) {
        List<Field> fields = fieldRepository.findBySupplierId(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier does not have any filed")
        );

        return fields.stream()
                .map(field -> modelMapper.map(field, FieldDto.class))
                .collect(Collectors.toList());
    }
}
