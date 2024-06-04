package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.payload.WarehouseDto;
import com.example.agriecommerce.payload.WarehouseResponse;
import com.example.agriecommerce.service.WarehouseService;
import com.example.agriecommerce.utils.AppConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "REST APIs for Warehouse")
@RestController
@RequestMapping("api/suppliers")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping("{supplierId}/warehouses")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<WarehouseDto> createWarehouse(@PathVariable("supplierId") Long id,
                                                        @RequestBody WarehouseDto warehouseDto) {
        WarehouseDto dto = warehouseService.createWarehouse(id, warehouseDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("{supplierId}/warehousese")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<WarehouseDto> createWarehouseEncrypt(@PathVariable("supplierId") Long id,
                                                          @RequestBody WarehouseDto warehouseDto) {
        WarehouseDto dto = warehouseService.createWarehouseEn(id, warehouseDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/warehouses/{id}")
    public ResponseEntity<WarehouseDto> getWarehouseById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(id));
    }

    @GetMapping("/{supplierId}/warehouses")
    public ResponseEntity<List<WarehouseDto>> getAllWarehousesBySupplierId(@PathVariable("supplierId") Long id) {
        return ResponseEntity.ok(warehouseService.getAllWarehousesBySupplierId(id));
    }

    @GetMapping("/warehouses")
    public ResponseEntity<WarehouseResponse> getWarehouseBySupplierId(
            @RequestParam("supplierId") Long id,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(warehouseService.getWarehousesBySupplierId(id, pageNo, pageSize, sortBy, sortDir));
    }

    @GetMapping("{supplierId}/search")
    public ResponseEntity<WarehouseResponse> searchWarehouse(
            @PathVariable("supplierId") Long id,
            @RequestParam(value = "query") String query,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(warehouseService.searchWarehouse(query, id, pageNo, pageSize, sortBy, sortDir));
    }

    @DeleteMapping("/{supplierId}/warehouses/{id}")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<ResultDto> deleteWarehouse(@PathVariable("supplierId") Long supplierId,
                                                     @PathVariable("id") Long warehouseId) {
        ResultDto result = warehouseService.deleteWarehouse(supplierId, warehouseId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{supplierId}/warehouses/{id}")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<WarehouseDto> updateWarehouse(@PathVariable("supplierId") Long supplierId,
                                                        @PathVariable("id") Long warehouseId,
                                                        @RequestBody WarehouseDto warehouseDto) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(supplierId, warehouseId, warehouseDto));
    }

    @PatchMapping("/{supplierId}/warehouses/{id}")
    @PreAuthorize(("hasRole('SUPPLIER')"))
    public ResponseEntity<WarehouseDto> updateState(@PathVariable("supplierId") Long supplierId,
                                                    @PathVariable("id") Long warehouseId,
                                                    @RequestBody ResultDto resultDto) {
        return ResponseEntity.ok(warehouseService.updateState(supplierId, warehouseId, resultDto));
    }
}
