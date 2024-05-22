package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.service.FieldService;
import com.example.agriecommerce.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/field")
public class FieldController {
    private final FieldService fieldService;

    @Autowired
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @PostMapping("{supplierId}")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<FieldDto> createNewCrops(@PathVariable("supplierId") Long supplierId,
                                                   @RequestBody FieldDto fieldDto) {
        FieldDto result = fieldService.createNewCrops(supplierId, fieldDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<FieldResponse> getAllFields(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(fieldService.getAllFields(pageNo, pageSize, sortBy, sortDir));
    }

    @PutMapping("{fieldId}")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<FieldDto> updateCrops(@PathVariable("fieldId") Long fieldId,
                                                @RequestBody FieldDto fieldDto) {
        return ResponseEntity.ok(fieldService.updateCropsField(fieldId, fieldDto));
    }

    @PatchMapping("{fieldId}/complete")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<FieldDto> completeCrops(@PathVariable("fieldId") Long fieldId) {
        return ResponseEntity.ok(fieldService.completeCrops(fieldId));
    }

    @PatchMapping("{fieldId}")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<FieldDto> updateYield(@PathVariable("fieldId") Long fieldId,
                                                @RequestBody FieldDto fieldDto) {
        return ResponseEntity.ok(fieldService.updateYield(fieldId, fieldDto));
    }

    @GetMapping("{fieldId}/detail")
    public ResponseEntity<FieldDto> getFieldById(@PathVariable("fieldId") Long fieldId) {
        return ResponseEntity.ok(fieldService.getFieldById(fieldId));
    }

    @GetMapping("{supplierId}")
    public ResponseEntity<List<FieldDto>> getAllFieldBySupplierId(@PathVariable("supplierId") Long supplierId) {
        return ResponseEntity.ok(fieldService.getAllFieldBySupplierId(supplierId));
    }

    @GetMapping("statistic")
    public ResponseEntity<ComparationDto> getStatisticField(@RequestParam("m") int month,
                                                            @RequestParam("y") int year) {
        return ResponseEntity.ok(fieldService.getStatisticField(month, year));
    }

    @GetMapping("chart")
    public ResponseEntity<List<LineChartGardenDto>> getChartData(@RequestParam("m") int month,
                                                                 @RequestParam("y") int year) {
        return ResponseEntity.ok(fieldService.getGardenSource(month, year));
    }

    @GetMapping("pie")
    public ResponseEntity<List<PieChartDto>> getPieChartData(@RequestParam("y") int year) {
        return ResponseEntity.ok(fieldService.getPieChart(year));
    }

}
