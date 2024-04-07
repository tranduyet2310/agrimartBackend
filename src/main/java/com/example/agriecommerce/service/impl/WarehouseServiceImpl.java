package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Supplier;
import com.example.agriecommerce.entity.Warehouse;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.WarehouseDto;
import com.example.agriecommerce.repository.SupplierRepository;
import com.example.agriecommerce.repository.WarehouseReposiotry;
import com.example.agriecommerce.service.WarehouseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseReposiotry warehouseReposiotry;
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public WarehouseServiceImpl(WarehouseReposiotry warehouseReposiotry, SupplierRepository supplierRepository, ModelMapper modelMapper) {
        this.warehouseReposiotry = warehouseReposiotry;
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public WarehouseDto createWarehouse(Long supplierId, WarehouseDto warehouseDto) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );

        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseName(warehouseDto.getWarehouseName());
        warehouse.setEmail(warehouseDto.getEmail());
        warehouse.setPhone(warehouseDto.getPhone());
        warehouse.setContact(warehouseDto.getContact());
        warehouse.setProvince(warehouseDto.getProvince());
        warehouse.setDistrict(warehouseDto.getDistrict());
        warehouse.setCommune(warehouseDto.getCommune());
        warehouse.setDetail(warehouseDto.getDetail());
        warehouse.setSupplier(supplier);

        Warehouse savedWarehouse = warehouseReposiotry.save(warehouse);

        List<Warehouse> warehouseList = supplier.getWarehouses();
        warehouseList.add(warehouse);
        supplier.setWarehouses(warehouseList);
        supplierRepository.save(supplier);

        return modelMapper.map(savedWarehouse, WarehouseDto.class);
    }

    @Override
    public WarehouseDto getWarehouseById(Long warehouseId) {
        Warehouse warehouse = warehouseReposiotry.findById(warehouseId).orElseThrow(
                () -> new ResourceNotFoundException("warehouse", "id", warehouseId)
        );
        return modelMapper.map(warehouse, WarehouseDto.class);
    }

    @Override
    public List<WarehouseDto> getAllWarehousesBySupplierId(Long supplierId) {
        List<Warehouse> warehouseList = warehouseReposiotry.findBySupplierId(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("Warehouse does not exists with supplier id: " + supplierId)
        );
        return warehouseList.stream()
                .map(warehouse -> modelMapper.map(warehouse, WarehouseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWarehouse(Long supplierId, Long warehouseId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        Warehouse warehouse = warehouseReposiotry.findById(warehouseId).orElseThrow(
                () -> new ResourceNotFoundException("warehouse", "id", warehouseId)
        );
        warehouseReposiotry.delete(warehouse);
    }

    @Override
    @Transactional
    public WarehouseDto updateWarehouse(Long supplierId, Long warehouseId, WarehouseDto warehouseDto) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        Warehouse warehouse = warehouseReposiotry.findById(warehouseId).orElseThrow(
                () -> new ResourceNotFoundException("warehouse", "id", warehouseId)
        );

        warehouse.setId(warehouseId);
        warehouse.setWarehouseName(warehouseDto.getWarehouseName());
        warehouse.setEmail(warehouseDto.getEmail());
        warehouse.setPhone(warehouseDto.getPhone());
        warehouse.setContact(warehouseDto.getContact());
        warehouse.setProvince(warehouseDto.getProvince());
        warehouse.setDistrict(warehouseDto.getDistrict());
        warehouse.setCommune(warehouseDto.getCommune());
        warehouse.setDetail(warehouseDto.getDetail());
        warehouse.setSupplier(supplier);

        Warehouse updatedWarehouse = warehouseReposiotry.save(warehouse);

        return modelMapper.map(updatedWarehouse, WarehouseDto.class);
    }
}
