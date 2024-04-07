package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.SupplierIntroDto;
import com.example.agriecommerce.service.SupplierIntroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/shop")
public class SupplierIntroController {
    private final SupplierIntroService supplierIntroService;

    @Autowired
    public SupplierIntroController(SupplierIntroService supplierIntroService) {
        this.supplierIntroService = supplierIntroService;
    }

    @PostMapping("{supplierId}")
    public ResponseEntity<SupplierIntroDto> createSupplierIntro(@PathVariable("supplierId") Long supplierId,
                                                                @RequestParam("description") String description,
                                                                @RequestParam("type") String type,
                                                                @RequestParam("file") MultipartFile[] multipartFiles) {
        SupplierIntroDto introDto = new SupplierIntroDto();
        introDto.setSupplierId(supplierId);
        introDto.setDescription(description);
        introDto.setType(type);

        List<MultipartFile> files = new ArrayList<>(Arrays.asList(multipartFiles));
        SupplierIntroDto resultSupplierIntroDto = supplierIntroService.createSupplierIntro(supplierId, introDto, files);
        return new ResponseEntity<>(resultSupplierIntroDto, HttpStatus.CREATED);
    }

    @GetMapping("{supplierId}")
    public ResponseEntity<List<SupplierIntroDto>> getAllSupplierIntro(@PathVariable("supplierId") Long supplierId) {
        return ResponseEntity.ok(supplierIntroService.getAllSupplierIntro(supplierId));
    }

    @PutMapping("{supplierId}/{introId}")
    public ResponseEntity<SupplierIntroDto> updateSupplierIntro(@PathVariable("supplierId") Long supplierId,
                                                                @PathVariable("introId") Long introId,
                                                                @RequestParam("description") String description,
                                                                @RequestParam("type") String type,
                                                                @RequestParam("file") MultipartFile[] multipartFiles) {
        SupplierIntroDto introDto = new SupplierIntroDto();
        introDto.setSupplierId(supplierId);
        introDto.setDescription(description);
        introDto.setType(type);

        List<MultipartFile> files = new ArrayList<>(Arrays.asList(multipartFiles));
        SupplierIntroDto resultSupplierIntroDto = supplierIntroService.updateSupplierIntro(supplierId, introId, introDto, files);

        return ResponseEntity.ok(resultSupplierIntroDto);
    }
}
