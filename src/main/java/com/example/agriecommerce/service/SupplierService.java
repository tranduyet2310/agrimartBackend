package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SupplierService {
    SupplierDto getSupplierById(Long supplierId);

    SupplierResponse getAllSuppliers(boolean status, int pageNo, int pageSize, String sortBy, String sortDir);

    ImageDto getSupplierAvatar(Long supplierId);

    SupplierDto updateAccountInfo(Long supplierId, SupplierDto supplierDto);

    SupplierDto updateGeneralInfo(Long supplierId, SupplierDto supplierDto);

    SupplierDto updateBankInfo(Long supplierId, SupplierDto supplierDto);

    SupplierDto updateSupplierAvatar(Long supplierId, MultipartFile file);

    SupplierDto changePassword(Long supplierId, PasswordDto passwordDto);
    Long getSupplierIdByEmail(String email);
    Boolean checkAccountStatus(String email);
    ResultDto updateRSAKey(Long supplierId, AESDto dto);
    AESDto getRSAPubKey(Long supplierId);
    SupplierDto updateFcmToken(Long supplierId, String fcmToken);
}
