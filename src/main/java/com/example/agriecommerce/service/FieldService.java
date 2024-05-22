package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.*;

import java.util.List;

public interface FieldService {
    FieldDto createNewCrops(Long supplierId, FieldDto fieldDto);
    FieldDto updateCropsField(Long fieldId, FieldDto fieldDto);
    FieldDto updateYield(Long fieldId, FieldDto fieldDto);
    FieldDto completeCrops(Long fieldId);
    List<FieldDto> getAllFieldBySupplierId(Long supplierId);
    FieldDto getFieldById(Long fieldId);
    FieldResponse getAllFields(int pageNo, int pageSize, String sortBy, String sortDir);
    ComparationDto getStatisticField(int month, int year);
    List<LineChartGardenDto> getGardenSource(int month, int year);
    List<PieChartDto> getPieChart(int year);
}
