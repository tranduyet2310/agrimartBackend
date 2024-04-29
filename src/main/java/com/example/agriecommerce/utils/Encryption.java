package com.example.agriecommerce.utils;

import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.payload.ProductDto;
import com.example.agriecommerce.payload.SupplierDto;
import com.example.agriecommerce.payload.WarehouseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class Encryption {
    private final ModelMapper modelMapper;
    private final AES aes;

    @Autowired
    public Encryption(ModelMapper modelMapper, AES aes) {
        this.modelMapper = modelMapper;
        this.aes = aes;
    }

    public WarehouseDto encryptWarehouseDto(WarehouseDto dto, String secretKey, String iv) {
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

    public WarehouseDto decryptWarehouseDto(WarehouseDto dto, String secretKey, String iv) {
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

    public SupplierDto encryptSupplierDto(SupplierDto dto, String secretKey, String iv) {
        SupplierDto supplierDto = new SupplierDto();

        try {
            aes.initFromString(secretKey, iv);

            supplierDto.setId(dto.getId());

            if (dto.getContactName() != null)
                supplierDto.setContactName(aes.encrypt(dto.getContactName()));

            if (dto.getShopName() != null)
                supplierDto.setShopName(aes.encrypt(dto.getShopName()));

            if (dto.getCccd() != null)
                supplierDto.setCccd(aes.encrypt(dto.getCccd()));

            if (dto.getEmail() != null)
                supplierDto.setEmail(aes.encrypt(dto.getEmail()));

            if (dto.getPhone() != null)
                supplierDto.setPhone(aes.encrypt(dto.getPhone()));

            if (dto.getTax_number() != null)
                supplierDto.setTax_number(aes.encrypt(dto.getTax_number()));

            if (dto.getAddress() != null)
                supplierDto.setAddress(aes.encrypt(dto.getAddress()));

            if (dto.getProvince() != null)
                supplierDto.setProvince(aes.encrypt(dto.getProvince()));

            if (dto.getPassword() != null)
                supplierDto.setPassword(aes.encrypt(dto.getPassword()));

            if (dto.getSellerType() != null)
                supplierDto.setSellerType(aes.encrypt(dto.getSellerType()));

            if (dto.getBankName() != null)
                supplierDto.setBankName(aes.encrypt(dto.getBankName()));

            if (dto.getBankAccountOwner() != null)
                supplierDto.setBankAccountOwner(aes.encrypt(dto.getBankAccountOwner()));

            if (dto.getBankBranchName() != null)
                supplierDto.setBankBranchName(aes.encrypt(dto.getBankBranchName()));

            if (dto.getBankAccountNumber() != null)
                supplierDto.setBankAccountNumber(aes.encrypt(dto.getBankAccountNumber()));

            if (dto.getAvatar() != null)
                supplierDto.setAvatar(dto.getAvatar());

        } catch (Exception e) {
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Failed to decrypt data");
        }

        return supplierDto;
    }

    public SupplierDto decryptSupplierDto(SupplierDto dto, String secretKey, String iv) {
        SupplierDto supplierDto = new SupplierDto();

        try {
            aes.initFromString(secretKey, iv);

            supplierDto.setId(dto.getId());

            if (dto.getContactName() != null)
                supplierDto.setContactName(aes.decrypt(dto.getContactName()));

            if (dto.getShopName() != null)
                supplierDto.setShopName(aes.decrypt(dto.getShopName()));

            if (dto.getCccd() != null)
                supplierDto.setCccd(aes.decrypt(dto.getCccd()));

            if (dto.getEmail() != null)
                supplierDto.setEmail(aes.decrypt(dto.getEmail()));

            if (dto.getPhone() != null)
                supplierDto.setPhone(aes.decrypt(dto.getPhone()));

            if (dto.getTax_number() != null)
                supplierDto.setTax_number(aes.decrypt(dto.getTax_number()));

            if (dto.getAddress() != null)
                supplierDto.setAddress(aes.decrypt(dto.getAddress()));

            if (dto.getProvince() != null)
                supplierDto.setProvince(aes.decrypt(dto.getProvince()));

            if (dto.getPassword() != null)
                supplierDto.setPassword(aes.decrypt(dto.getPassword()));

            if (dto.getSellerType() != null)
                supplierDto.setSellerType(aes.decrypt(dto.getSellerType()));

            if (dto.getBankName() != null)
                supplierDto.setBankName(aes.decrypt(dto.getBankName()));

            if (dto.getBankAccountOwner() != null)
                supplierDto.setBankAccountOwner(aes.decrypt(dto.getBankAccountOwner()));

            if (dto.getBankBranchName() != null)
                supplierDto.setBankBranchName(aes.decrypt(dto.getBankBranchName()));

            if (dto.getBankAccountNumber() != null)
                supplierDto.setBankAccountNumber(aes.decrypt(dto.getBankAccountNumber()));

            if (dto.getAvatar() != null)
                supplierDto.setAvatar(dto.getAvatar());

        } catch (Exception e) {
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Failed to decrypt data");
        }

        return supplierDto;
    }

    public ProductDto encryptProductDto(ProductDto sourceDto, String secretKey, String iv){
        ProductDto destDto = new ProductDto();

        try {
            aes.initFromString(secretKey, iv);

            destDto.setId(sourceDto.getId());
            destDto.setProductName(aes.encrypt(sourceDto.getProductName()));
            destDto.setDescription(aes.encrypt(sourceDto.getDescription()));
            destDto.setStandardPrice(sourceDto.getStandardPrice());
            destDto.setDiscountPrice(sourceDto.getDiscountPrice());
            destDto.setQuantity(sourceDto.getQuantity());
            destDto.setCategoryName(aes.encrypt(sourceDto.getCategoryName()));
            destDto.setSubCategoryName(aes.encrypt(sourceDto.getSubCategoryName()));
            destDto.setWarehouseName(aes.encrypt(sourceDto.getWarehouseName()));
            destDto.setSupplierShopName(aes.encrypt(sourceDto.getSupplierShopName()));
            destDto.setImages(sourceDto.getImages());
            destDto.setActive(sourceDto.isActive());
            destDto.setAvailable(sourceDto.isAvailable());
            destDto.setNew(sourceDto.isNew());
            destDto.setSupplierProvince(aes.encrypt(sourceDto.getSupplierProvince()));
            destDto.setSupplierId(sourceDto.getSupplierId());
            destDto.setSold(sourceDto.getSold());

        }catch (Exception e){
            throw new AgriMartException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to encrypt data");
        }

        return destDto;
    }

    public ProductDto decryptProductDto(ProductDto sourceDto, String secretKey, String iv){
        ProductDto destDto = new ProductDto();

        try {
            aes.initFromString(secretKey, iv);

            destDto.setId(sourceDto.getId());
            String productName = sourceDto.getProductName();
            destDto.setProductName(aes.decrypt(productName));
            destDto.setDescription(aes.decrypt(sourceDto.getDescription()));
            destDto.setStandardPrice(sourceDto.getStandardPrice());
            destDto.setDiscountPrice(sourceDto.getDiscountPrice());
            destDto.setQuantity(sourceDto.getQuantity());
            destDto.setCategoryName(aes.decrypt(sourceDto.getCategoryName()));
            destDto.setSubCategoryName(aes.decrypt(sourceDto.getSubCategoryName()));
            destDto.setWarehouseName(aes.decrypt(sourceDto.getWarehouseName()));
//            destDto.setSupplierShopName(aes.decrypt(sourceDto.getSupplierShopName()));
            destDto.setImages(sourceDto.getImages());
            destDto.setActive(sourceDto.isActive());
            destDto.setAvailable(sourceDto.isAvailable());
            destDto.setNew(sourceDto.isNew());
//            destDto.setSupplierProvince(aes.decrypt(sourceDto.getSupplierProvince()));
//            destDto.setSupplierId(sourceDto.getSupplierId());
//            destDto.setSold(sourceDto.getSold());

        }catch (Exception e){
            e.printStackTrace();
            throw new AgriMartException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to decrypt data");
        }

        return destDto;
    }
}
