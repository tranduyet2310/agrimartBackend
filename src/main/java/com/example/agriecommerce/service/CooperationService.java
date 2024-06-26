package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.*;

import java.util.List;

public interface CooperationService {
    CooperationDto createCooperation(Long supplierId, CooperationDto cooperationDto);
    CooperationDto updateCooperation(Long cooperationId, CooperationDto cooperationDto);
    CooperationDto getCooperationById(Long cooperationId);
    List<CooperationDto> getCooperationBySupplierId(Long supplierId);
    CooperationResponse getCooperationBySupplierIdV2(Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir);
    CooperationResponse getAllCooperations(int pageNo, int pageSize, String sortBy, String sortDir);
    CooperationResponse getCooperationSortByField(Long supplierId, Long fieldId, int pageNo, int pageSize, String sortBy, String sortDir);
    List<CooperationDto> getCooperationByUserId(Long userId);
    ResultDto calculateCurrentTotal(Long fieldId, Long supplierId);
    CooperationDto updateStatus(Long cooperationId, CooperationDto cooperationDto);
    CooperationDto updateAddress(Long cooperationId, Long addressId);
    CooperationDto updatePayment(Long cooperationId, CooperationDto cooperationDto);
    ResultDto calculateRemainingCooperation(Long fieldId);
    ComparationDto getStatisticCooperation(int month, int year);
}
