package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.FieldDto;

import java.util.List;

public interface FieldService {
    FieldDto createNewCrops(Long supplierId, FieldDto fieldDto);
    FieldDto updateCropsField(Long fieldId, FieldDto fieldDto);
    List<FieldDto> getAllFieldBySupplierId(Long supplierId);
}
