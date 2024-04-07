package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.WarehouseDto;
import com.example.agriecommerce.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/suppliers")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping("{supplierId}/warehouses")
    public ResponseEntity<WarehouseDto> createWarehouse(@PathVariable("supplierId") Long id,
                                                        @RequestBody WarehouseDto warehouseDto) {
        WarehouseDto dto = warehouseService.createWarehouse(id, warehouseDto);
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

    @DeleteMapping("/{supplierId}/warehouses/{id}")
    public ResponseEntity<String> deleteWarehouse(@PathVariable("supplierId") Long supplierId,
                                                  @PathVariable("id") Long warehouseId) {
        warehouseService.deleteWarehouse(supplierId, warehouseId);
        return ResponseEntity.ok("Warehouse deleted successfully");
    }

    @PutMapping("/{supplierId}/warehouses/{id}")
    public ResponseEntity<WarehouseDto> updateWarehouse(@PathVariable("supplierId") Long supplierId,
                                                        @PathVariable("id") Long warehouseId,
                                                        @RequestBody WarehouseDto warehouseDto) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(supplierId, warehouseId, warehouseDto));
    }
}
