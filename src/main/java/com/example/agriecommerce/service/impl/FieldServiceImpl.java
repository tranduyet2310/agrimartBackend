package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.*;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.FieldDto;
import com.example.agriecommerce.repository.CooperationRepository;
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
    private final CooperationRepository cooperationRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public FieldServiceImpl(FieldRepository fieldRepository,
                            SupplierRepository supplierRepository,
                            ModelMapper modelMapper,
                            CooperationRepository cooperationRepository) {
        this.fieldRepository = fieldRepository;
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.cooperationRepository = cooperationRepository;
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
        field.setIsComplete(false);
        field.setEstimatePrice(0L);
        field.setEstimateYield(0.0);

        Field savedField = fieldRepository.save(field);

        return modelMapper.map(savedField, FieldDto.class);
    }

    @Override
    public FieldDto updateCropsField(Long fieldId, FieldDto fieldDto) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(
                () -> new ResourceNotFoundException("field does not exists")
        );

        field.setCropsName(fieldDto.getCropsName());
        field.setArea(fieldDto.getArea());
        field.setCropsType(fieldDto.getCropsType());
        field.setSeason(fieldDto.getSeason());

        Field updatedField = fieldRepository.save(field);

        return modelMapper.map(updatedField, FieldDto.class);
    }

    @Override
    public FieldDto updateYield(Long fieldId, FieldDto fieldDto) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(
                () -> new ResourceNotFoundException("field does not exists")
        );

        Double currentYield = field.getEstimateYield();
        Double newYieldValue = fieldDto.getEstimateYield();

        if (newYieldValue < currentYield){
            Double gapsValue = currentYield - newYieldValue;
            List<Cooperation> cooperationList = cooperationRepository.findByFieldIdAndDateCreated(fieldId);
            terminatedCooperation(cooperationList, gapsValue);
        }

        field.setEstimateYield(fieldDto.getEstimateYield());
        field.setEstimatePrice(fieldDto.getEstimatePrice());

        Field updatedField = fieldRepository.save(field);

        return modelMapper.map(updatedField, FieldDto.class);
    }

    @Override
    public FieldDto completeCrops(Long fieldId) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(
                () -> new ResourceNotFoundException("field does not exists")
        );
        field.setIsComplete(true);
        Field savedField = fieldRepository.save(field);
        return modelMapper.map(savedField, FieldDto.class);
    }

    @Override
    public List<FieldDto> getAllFieldBySupplierId(Long supplierId) {
        List<Field> fields = fieldRepository.findByIdAndIsComplete(supplierId, false).orElseThrow(
                () -> new ResourceNotFoundException("supplier does not have any filed")
        );

        return fields.stream()
                .map(field -> modelMapper.map(field, FieldDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FieldDto getFieldById(Long fieldId) {
        Field field = fieldRepository.findById(fieldId).orElseThrow(
                () -> new ResourceNotFoundException("field does not exists")
        );
        return modelMapper.map(field, FieldDto.class);
    }

    private void terminatedCooperation(List<Cooperation> cooperationList, Double gapsValue){
        for (Cooperation cooperation : cooperationList){
            Double currentYield = cooperation.getRequireYield();
            if (gapsValue > 0){
                if (currentYield >= gapsValue){
                    cooperation.setCooperationStatus(OrderStatus.CANCELLED);
                    cooperation.setRequireYield(0.0);
                    cooperationRepository.save(cooperation);
                    break;
                } else {
                    gapsValue -= currentYield;
                    cooperation.setCooperationStatus(OrderStatus.CANCELLED);
                    cooperation.setRequireYield(0.0);
                    cooperationRepository.save(cooperation);
                }
            }
        }
    }
}
