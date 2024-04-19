package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.ProductResponse;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.payload.WarehouseDto;
import com.example.agriecommerce.payload.WarehouseResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WarehouseService {

    WarehouseDto createWarehouse(Long supplierId, WarehouseDto warehouseDto);

    WarehouseDto createWarehouseEn(Long supplierId, WarehouseDto warehouseDto);

    WarehouseDto getWarehouseById(Long warehouseId);

    List<WarehouseDto> getAllWarehousesBySupplierId(Long supplierId);

    WarehouseResponse getWarehousesBySupplierId(Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir);
    WarehouseResponse searchWarehouse(String query, Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir);
    ResultDto deleteWarehouse(Long supplierId, Long warehouseId);

    WarehouseDto updateWarehouse(Long supplierId, Long warehouseId, WarehouseDto warehouseDto);
    WarehouseDto updateState(Long supplierId, Long warehouseId, ResultDto resultDto);
}
