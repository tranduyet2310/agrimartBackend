package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.SupplierIntroDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SupplierIntroService {
    SupplierIntroDto createSupplierIntro(Long supplierId, SupplierIntroDto introDto, List<MultipartFile> files);
    List<SupplierIntroDto> getAllSupplierIntro(Long supplierId);
    SupplierIntroDto updateSupplierIntro(Long supplierId, Long introId, SupplierIntroDto introDto, List<MultipartFile> files);
    SupplierIntroDto updateDescriptionIntro(Long introId, SupplierIntroDto introDto);
}
