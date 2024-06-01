package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.FieldDetailDto;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.service.FieldDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/field_detail")
public class FieldDetailController {
    private final FieldDetailService fieldDetailService;

    @Autowired
    public FieldDetailController(FieldDetailService fieldDetailService) {
        this.fieldDetailService = fieldDetailService;
    }

    @PostMapping("{fieldId}")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<FieldDetailDto> createFieldDetail(@PathVariable("fieldId") Long fieldId,
                                                            @RequestBody FieldDetailDto fieldDetailDto) {
        FieldDetailDto result = fieldDetailService.createFieldDetail(fieldId, fieldDetailDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("{fieldId}")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<FieldDetailDto> updateFieldDetail(@PathVariable("fieldId") Long fieldId,
                                                            @RequestBody FieldDetailDto fieldDetailDto) {
        return ResponseEntity.ok(fieldDetailService.updateFieldDetail(fieldId, fieldDetailDto));
    }

    @DeleteMapping("{fieldDetailId}")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<ResultDto> deleteFieldDetail(@PathVariable("fieldDetailId") Long fieldDetailId) {
        return ResponseEntity.ok(fieldDetailService.deleteFieldDetail(fieldDetailId));
    }
}
