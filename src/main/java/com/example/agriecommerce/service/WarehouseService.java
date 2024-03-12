package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.WarehouseDto;

import java.util.List;

public interface WarehouseService {
    WarehouseDto createWarehouse(Long supplierId, WarehouseDto warehouseDto);

    WarehouseDto getWarehouseById(Long warehouseId);

    List<WarehouseDto> getAllWarehousesBySupplierId(Long supplierId);

    void deleteWarehouse(Long supplierId, Long warehouseId);

    WarehouseDto updateWarehouse(Long supplierId, Long warehouseId, WarehouseDto warehouseDto);
}
