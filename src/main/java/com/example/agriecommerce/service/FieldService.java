package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.FieldDto;

import java.util.List;

public interface FieldService {
    FieldDto createNewCrops(Long supplierId, FieldDto fieldDto);
    FieldDto updateCropsField(Long fieldId, FieldDto fieldDto);
    FieldDto updateYield(Long fieldId, FieldDto fieldDto);
    FieldDto completeCrops(Long fieldId);
    List<FieldDto> getAllFieldBySupplierId(Long supplierId);
}
