package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Supplier;
import com.example.agriecommerce.entity.Warehouse;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.payload.WarehouseDto;
import com.example.agriecommerce.payload.WarehouseResponse;
import com.example.agriecommerce.repository.SupplierRepository;
import com.example.agriecommerce.repository.WarehouseReposiotry;
import com.example.agriecommerce.service.WarehouseService;
import com.example.agriecommerce.utils.encrypt.AES;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseReposiotry warehouseReposiotry;
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    private final AES aes;

    @Autowired
    public WarehouseServiceImpl(WarehouseReposiotry warehouseReposiotry,
                                SupplierRepository supplierRepository,
                                ModelMapper modelMapper,
                                AES aes) {
        this.warehouseReposiotry = warehouseReposiotry;
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.aes = aes;
    }

    @Transactional
    @Override
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
        warehouse.setActive(warehouseDto.isActive());

        Warehouse savedWarehouse = warehouseReposiotry.save(warehouse);

        List<Warehouse> warehouseList = supplier.getWarehouses();
        warehouseList.add(warehouse);
        supplier.setWarehouses(warehouseList);
        supplierRepository.save(supplier);

        return modelMapper.map(savedWarehouse, WarehouseDto.class);
    }

    @Transactional
    @Override
    public WarehouseDto createWarehouseEn(Long supplierId, WarehouseDto warehouseDto) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );

        String secretKey = supplier.getAesKey();
        String iv = supplier.getIv();

        WarehouseDto decryptedDto = decryptWarehouseDto(warehouseDto, secretKey, iv);

        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseName(decryptedDto.getWarehouseName());
        warehouse.setEmail(decryptedDto.getEmail());
        warehouse.setPhone(decryptedDto.getPhone());
        warehouse.setContact(decryptedDto.getContact());
        warehouse.setProvince(decryptedDto.getProvince());
        warehouse.setDistrict(decryptedDto.getDistrict());
        warehouse.setCommune(decryptedDto.getCommune());
        warehouse.setDetail(decryptedDto.getDetail());
        warehouse.setSupplier(supplier);
        warehouse.setActive(decryptedDto.isActive());

        Warehouse savedWarehouse = warehouseReposiotry.save(warehouse);

        List<Warehouse> warehouseList = supplier.getWarehouses();
        warehouseList.add(warehouse);
        supplier.setWarehouses(warehouseList);
        supplierRepository.save(supplier);

        WarehouseDto result = modelMapper.map(savedWarehouse, WarehouseDto.class);

        return encryptWarehouseDto(result, secretKey, iv);
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
                .toList();
    }

    @Override
    @Transactional
    public ResultDto deleteWarehouse(Long supplierId, Long warehouseId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        Warehouse warehouse = warehouseReposiotry.findById(warehouseId).orElseThrow(
                () -> new ResourceNotFoundException("warehouse", "id", warehouseId)
        );
        warehouseReposiotry.delete(warehouse);
        return new ResultDto(true, "Delete warehouse successfully");
    }

    @Transactional
    @Override
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
        warehouse.setActive(warehouseDto.isActive());

        Warehouse updatedWarehouse = warehouseReposiotry.save(warehouse);

        return modelMapper.map(updatedWarehouse, WarehouseDto.class);
    }

    @Override
    @Transactional
    public WarehouseDto updateState(Long supplierId, Long warehouseId, ResultDto resultDto) {
        Warehouse warehouse = warehouseReposiotry.findById(warehouseId).orElseThrow(
                () -> new ResourceNotFoundException("warehouse", "id", warehouseId)
        );

        warehouse.setId(warehouseId);
        warehouse.setActive(resultDto.isSuccessful());

        Warehouse updatedWarehouse = warehouseReposiotry.save(warehouse);

        return modelMapper.map(updatedWarehouse, WarehouseDto.class);
    }

    @Override
    public WarehouseResponse getWarehousesBySupplierId(Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Warehouse> warehousePage = warehouseReposiotry.findBySupplierId(supplierId, pageable).orElseThrow(
                () -> new ResourceNotFoundException("Don't have any product with subcategory id " + supplierId)
        );

        List<Warehouse> warehouses = warehousePage.getContent();
        List<WarehouseDto> content = warehouses.stream().map(warehouse -> modelMapper.map(warehouse, WarehouseDto.class)).toList();

        WarehouseResponse warehouseResponse = new WarehouseResponse();
        warehouseResponse.setContent(content);
        warehouseResponse.setPageNo(warehousePage.getNumber());
        warehouseResponse.setPageSize(warehousePage.getSize());
        warehouseResponse.setTotalElements(warehousePage.getTotalElements());
        warehouseResponse.setTotalPage(warehousePage.getTotalPages());
        warehouseResponse.setLast(warehousePage.isLast());

        return warehouseResponse;
    }

    @Override
    public WarehouseResponse searchWarehouse(String query, Long supplierId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Warehouse> warehousePage = warehouseReposiotry.searchWarehouse(query, supplierId, pageable).orElseThrow(
                () -> new ResourceNotFoundException("There are no results for that condition " + query)
        );

        List<Warehouse> warehouses = warehousePage.getContent();
        List<WarehouseDto> content = warehouses.stream().map(warehouse -> modelMapper.map(warehouse, WarehouseDto.class)).toList();

        WarehouseResponse warehouseResponse = new WarehouseResponse();
        warehouseResponse.setContent(content);
        warehouseResponse.setPageNo(warehousePage.getNumber());
        warehouseResponse.setPageSize(warehousePage.getSize());
        warehouseResponse.setTotalElements(warehousePage.getTotalElements());
        warehouseResponse.setTotalPage(warehousePage.getTotalPages());
        warehouseResponse.setLast(warehousePage.isLast());

        return warehouseResponse;
    }

    private WarehouseDto encryptWarehouseDto(WarehouseDto dto, String secretKey, String iv) {
        WarehouseDto warehouseDto = new WarehouseDto();

        try {
            aes.initFromString(secretKey, iv);

            warehouseDto.setId(dto.getId());
            warehouseDto.setWarehouseName(aes.encrypt(dto.getWarehouseName()));
            warehouseDto.setEmail(aes.encrypt(dto.getEmail()));
            warehouseDto.setPhone(aes.encrypt(dto.getPhone()));
            warehouseDto.setContact(aes.encrypt(dto.getContact()));
            warehouseDto.setProvince(aes.encrypt(dto.getProvince()));
            warehouseDto.setDistrict(aes.encrypt(dto.getDistrict()));
            warehouseDto.setCommune(aes.encrypt(dto.getCommune()));
            warehouseDto.setDetail(aes.encrypt(dto.getDetail()));
            warehouseDto.setSupplierContactName(aes.encrypt(dto.getSupplierContactName()));
            warehouseDto.setActive(dto.isActive());
        } catch (Exception e) {
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Failed to decrypt data");
        }

        return warehouseDto;
    }

    private WarehouseDto decryptWarehouseDto(WarehouseDto dto, String secretKey, String iv) {
        WarehouseDto warehouseDto = new WarehouseDto();

        try {
            aes.initFromString(secretKey, iv);
            warehouseDto.setWarehouseName(aes.decrypt(dto.getWarehouseName()));
            warehouseDto.setEmail(aes.decrypt(dto.getEmail()));
            warehouseDto.setPhone(aes.decrypt(dto.getPhone()));
            warehouseDto.setContact(aes.decrypt(dto.getContact()));
            warehouseDto.setProvince(aes.decrypt(dto.getProvince()));
            warehouseDto.setDistrict(aes.decrypt(dto.getDistrict()));
            warehouseDto.setCommune(aes.decrypt(dto.getCommune()));
            warehouseDto.setDetail(aes.decrypt(dto.getDetail()));
            warehouseDto.setActive(dto.isActive());
        } catch (Exception e) {
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Failed to decrypt data");
        }

        return warehouseDto;
    }
}
