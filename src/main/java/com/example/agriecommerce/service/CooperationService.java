package com.example.agriecommerce.service;

import com.example.agriecommerce.entity.OrderStatus;
import com.example.agriecommerce.payload.CooperationDto;
import com.example.agriecommerce.payload.ResultDto;

import java.util.List;

public interface CooperationService {
    CooperationDto createCooperation(Long supplierId, CooperationDto cooperationDto);
    CooperationDto updateCooperation(Long cooperationId, CooperationDto cooperationDto);
    List<CooperationDto> getCooperationBySupplierId(Long supplierId);
    List<CooperationDto> getCooperationByUserId(Long userId);
    ResultDto calculateCurrentTotal(Long fieldId, Long supplierId);

    CooperationDto updateStatus(Long cooperationId, CooperationDto cooperationDto);
}
