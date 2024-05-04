package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SupplierDto> getSupplier(@PathVariable("id") Long supplierId) {
        return ResponseEntity.ok(supplierService.getSupplierById(supplierId));
    }

    @GetMapping
    public ResponseEntity<List<SupplierDto>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSupplier());
    }

    @PatchMapping("{id}/general")
    public ResponseEntity<SupplierDto> updateGeneralInfo(@PathVariable("id") Long supplierId,
                                                         @RequestBody SupplierDto supplierDto) {
        return ResponseEntity.ok(supplierService.updateGeneralInfo(supplierId, supplierDto));
    }

    @PatchMapping("{id}/account")
    public ResponseEntity<SupplierDto> updateAccountInfo(@PathVariable("id") Long supplierId,
                                                         @RequestBody SupplierDto supplierDto) {
        return ResponseEntity.ok(supplierService.updateAccountInfo(supplierId, supplierDto));
    }

    @PatchMapping("{id}/bank")
    public ResponseEntity<SupplierDto> updateBankInfo(@PathVariable("id") Long supplierId,
                                                      @RequestBody SupplierDto supplierDto) {
        return ResponseEntity.ok(supplierService.updateBankInfo(supplierId, supplierDto));
    }

    @PatchMapping("{id}/avatar")
    public ResponseEntity<SupplierDto> updateSupplierAvatar(@PathVariable("id") Long supplierId,
                                                            @RequestParam("file") MultipartFile multipartFile) {
        return ResponseEntity.ok(supplierService.updateSupplierAvatar(supplierId, multipartFile));
    }

    @PatchMapping("{id}/password")
    public ResponseEntity<SupplierDto> changePassword(@PathVariable("id") Long supplierId,
                                                      @RequestBody PasswordDto passwordDto) {
        return ResponseEntity.ok(supplierService.changePassword(supplierId, passwordDto));
    }

    @GetMapping("{id}/image")
    public ResponseEntity<ImageDto> getSupplierAvatar(@PathVariable("id") Long supplierId){
        return ResponseEntity.ok(supplierService.getSupplierAvatar(supplierId));
    }

    @PatchMapping("{id}/rsa")
    public ResponseEntity<ResultDto> updateRSAPubKey(@PathVariable("id") Long supplierId,
                                                       @RequestBody AESDto dto){
        return ResponseEntity.ok(supplierService.updateRSAKey(supplierId, dto));
    }

    @GetMapping("{id}/rsa")
    public ResponseEntity<AESDto> getRSAPubKey(@PathVariable("id") Long supplierId){
        return ResponseEntity.ok(supplierService.getRSAPubKey(supplierId));
    }
}
