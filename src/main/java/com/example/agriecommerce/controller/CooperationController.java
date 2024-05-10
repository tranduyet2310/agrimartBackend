package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.CooperationDto;
import com.example.agriecommerce.payload.CooperationResponse;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.service.CooperationService;
import com.example.agriecommerce.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<CooperationDto> createCooperation(@PathVariable("supplierId") Long supplierId,
                                                            @RequestBody CooperationDto cooperationDto) {
        CooperationDto result = cooperationService.createCooperation(supplierId, cooperationDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("{cooperationId}")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<CooperationDto> updateCooperation(@PathVariable("cooperationId") Long cooperationId,
                                                            @RequestBody CooperationDto cooperationDto) {
        return ResponseEntity.ok(cooperationService.updateCooperation(cooperationId, cooperationDto));
    }

    @GetMapping("{supplierId}/list")
    public ResponseEntity<List<CooperationDto>> getAllCooperationsBySupplierId(@PathVariable("supplierId") Long supplierId) {
        return ResponseEntity.ok(cooperationService.getCooperationBySupplierId(supplierId));
    }

    @GetMapping("{supplierId}/list/v2")
    public ResponseEntity<CooperationResponse> getAllCooperationsBySupplierIdV2(
            @PathVariable("supplierId") Long supplierId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return ResponseEntity.ok(cooperationService.getCooperationBySupplierIdV2(supplierId, pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("{supplierId}/list/{fieldId}")
    public ResponseEntity<CooperationResponse> getAllCooperationsByFieldName(
            @PathVariable("supplierId") Long supplierId,
            @PathVariable("fieldId") Long fieldId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return ResponseEntity.ok(cooperationService.getCooperationSortByField(supplierId, fieldId, pageNo, pageSize, sortBy, sortDir));
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

    @PatchMapping("{cooperationId}") // both
    public ResponseEntity<CooperationDto> updateCooperationStatus(@PathVariable("cooperationId") Long cooperationId,
                                                                  @RequestBody CooperationDto cooperationDto) {
        return ResponseEntity.ok(cooperationService.updateStatus(cooperationId, cooperationDto));
    }

    @PatchMapping("{cooperationId}/{addressId}")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<CooperationDto> updateAddress(@PathVariable("cooperationId") Long cooperationId,
                                                        @PathVariable("addressId") Long addressId) {
        return ResponseEntity.ok(cooperationService.updateAddress(cooperationId, addressId));
    }

    @PatchMapping("{cooperationId}/payment")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<CooperationDto> updatePayment(@PathVariable("cooperationId") Long cooperationId,
                                                        @RequestBody CooperationDto cooperationDto) {
        return ResponseEntity.ok(cooperationService.updatePayment(cooperationId, cooperationDto));
    }

    @GetMapping("{cooperationId}/general")
    public ResponseEntity<CooperationDto> getCooperationById(@PathVariable("cooperationId") Long cooperationId){
        return ResponseEntity.ok(cooperationService.getCooperationById(cooperationId));
    }
}
