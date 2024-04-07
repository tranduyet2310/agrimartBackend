package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.CooperationDto;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.service.CooperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cooperation")
public class CooperationController {
    private final CooperationService cooperationService;

    @Autowired
    public CooperationController(CooperationService cooperationService) {
        this.cooperationService = cooperationService;
    }

    @PostMapping("{supplierId}")
    public ResponseEntity<CooperationDto> createCooperation(@PathVariable("supplierId") Long supplierId,
                                                            @RequestBody CooperationDto cooperationDto) {
        CooperationDto result = cooperationService.createCooperation(supplierId, cooperationDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("{cooperationId}")
    public ResponseEntity<CooperationDto> updateCooperation(@PathVariable("cooperationId") Long cooperationId,
                                                            @RequestBody CooperationDto cooperationDto) {
        return ResponseEntity.ok(cooperationService.updateCooperation(cooperationId, cooperationDto));
    }

    @GetMapping("{supplierId}/list")
    public ResponseEntity<List<CooperationDto>> getAllCooperationsBySupplierId(@PathVariable("supplierId") Long supplierId) {
        return ResponseEntity.ok(cooperationService.getCooperationBySupplierId(supplierId));
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<CooperationDto>> getAllCooperationsByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(cooperationService.getCooperationByUserId(userId));
    }

    @GetMapping("{fieldId}/calculate/{supplierId}")
    public ResponseEntity<ResultDto> calculateCurrentTotal(@PathVariable("fieldId") Long fieldId,
                                                           @PathVariable("supplierId") Long supplierId) {
        return ResponseEntity.ok(cooperationService.calculateCurrentTotal(fieldId, supplierId));
    }

    @PatchMapping("{cooperationId}")
    public ResponseEntity<CooperationDto> updateCooperationStatus(@PathVariable("cooperationId") Long cooperationId,
                                                                  @RequestBody CooperationDto cooperationDto) {
        return ResponseEntity.ok(cooperationService.updateStatus(cooperationId, cooperationDto));
    }
}
