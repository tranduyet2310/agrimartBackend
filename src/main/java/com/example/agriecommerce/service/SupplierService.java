package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.PasswordDto;
import com.example.agriecommerce.payload.SupplierDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SupplierService {
    SupplierDto getSupplierById(Long supplierId);
    List<SupplierDto> getAllSupplier();

    SupplierDto updateAccountInfo(Long supplierId, SupplierDto supplierDto);
    SupplierDto updateGeneralInfo(Long supplierId, SupplierDto supplierDto);
    SupplierDto updateBankInfo(Long supplierId, SupplierDto supplierDto);

    SupplierDto updateSupplierAvatar(Long supplierId, MultipartFile file);

    SupplierDto changePassword(Long supplierId, PasswordDto passwordDto);
}
