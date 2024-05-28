package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.service.SupplierService;
import com.example.agriecommerce.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("{id}")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<SupplierDto> getSupplierById(@PathVariable("id") Long supplierId) {
        return ResponseEntity.ok(supplierService.getSupplierById(supplierId));
    }
    @GetMapping("{id}/info")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<SupplierDto> getSupplierInfoById(@PathVariable("id") Long supplierId) {
        return ResponseEntity.ok(supplierService.getSupplierInfoById(supplierId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierResponse> getAllSuppliers(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(supplierService.getAllSuppliers(true, pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("approval")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierResponse> getApprovalSuppliers(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(supplierService.getAllSuppliers(false, pageNo, pageSize, sortBy, sortDir));
    }

    @PatchMapping("{id}/status")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<ResultDto> updateAccountStatus(@PathVariable("id") Long supplierId,
                                                         @RequestParam("status") boolean status) {
        return ResponseEntity.ok(supplierService.updateAccountStatus(supplierId, status));
    }

    @PatchMapping("{id}/general")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<SupplierDto> updateGeneralInfo(@PathVariable("id") Long supplierId,
                                                         @RequestBody SupplierDto supplierDto) {
        return ResponseEntity.ok(supplierService.updateGeneralInfo(supplierId, supplierDto));
    }

    @PatchMapping("{id}/account")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<SupplierDto> updateAccountInfo(@PathVariable("id") Long supplierId,
                                                         @RequestBody SupplierDto supplierDto) {
        return ResponseEntity.ok(supplierService.updateAccountInfo(supplierId, supplierDto));
    }

    @PatchMapping("{id}/bank")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<SupplierDto> updateBankInfo(@PathVariable("id") Long supplierId,
                                                      @RequestBody SupplierDto supplierDto) {
        return ResponseEntity.ok(supplierService.updateBankInfo(supplierId, supplierDto));
    }

    @PatchMapping("{id}/avatar")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<SupplierDto> updateSupplierAvatar(@PathVariable("id") Long supplierId,
                                                            @RequestParam("file") MultipartFile multipartFile) {
        return ResponseEntity.ok(supplierService.updateSupplierAvatar(supplierId, multipartFile));
    }

    @PatchMapping("{id}/password")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<SupplierDto> changePassword(@PathVariable("id") Long supplierId,
                                                      @RequestBody PasswordDto passwordDto) {
        return ResponseEntity.ok(supplierService.changePassword(supplierId, passwordDto));
    }

    @GetMapping("{id}/image")
    public ResponseEntity<ImageDto> getSupplierAvatar(@PathVariable("id") Long supplierId){
        return ResponseEntity.ok(supplierService.getSupplierAvatar(supplierId));
    }

    @PatchMapping("{id}/rsa")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<ResultDto> updateRSAPubKey(@PathVariable("id") Long supplierId,
                                                       @RequestBody AESDto dto){
        return ResponseEntity.ok(supplierService.updateRSAKey(supplierId, dto));
    }

    @GetMapping("{id}/rsa")
    public ResponseEntity<AESDto> getRSAPubKey(@PathVariable("id") Long supplierId){
        return ResponseEntity.ok(supplierService.getRSAPubKey(supplierId));
    }

    @PatchMapping("{id}/fcm")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<SupplierDto> updateFcmToken(@PathVariable("id") Long supplierId,
                                                  @RequestParam("token") String fcmToken) {
        return ResponseEntity.ok(supplierService.updateFcmToken(supplierId, fcmToken));
    }

    @GetMapping("register")
    public ResponseEntity<ResultDto> countRegisterAccount(@RequestParam("y") int year){
        return ResponseEntity.ok(supplierService.countRegisterAccount(year));
    }

    @GetMapping("statistic")
    public ResponseEntity<ComparationDto> getStatisticSupplier(@RequestParam("m") int month,
                                                               @RequestParam("y") int year){
        return ResponseEntity.ok(supplierService.getStatisticSupplier(month, year));
    }
}
