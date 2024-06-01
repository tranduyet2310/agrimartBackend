package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.FieldDetailDto;
import com.example.agriecommerce.payload.ResultDto;

public interface FieldDetailService {
    FieldDetailDto createFieldDetail(Long fieldId, FieldDetailDto fieldDetailDto);
    FieldDetailDto updateFieldDetail(Long fieldId, FieldDetailDto fieldDetailDto);
    ResultDto deleteFieldDetail(Long fieldDetailId);
}
