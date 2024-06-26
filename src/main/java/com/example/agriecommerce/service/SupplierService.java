package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.*;
import org.springframework.web.multipart.MultipartFile;

public interface SupplierService {
    SupplierDto getSupplierById(Long supplierId);
    SupplierDto getSupplierByIdV2(Long supplierId);
    SupplierDto getSupplierInfoById(Long supplierId);

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
    ResultDto updateAccountStatus(Long supplierId, boolean state);
    ResultDto countRegisterAccount(int year);
    ComparationDto getStatisticSupplier(int month, int year);
}
