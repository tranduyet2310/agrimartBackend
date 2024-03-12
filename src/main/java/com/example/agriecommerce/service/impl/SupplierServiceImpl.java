package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Image;
import com.example.agriecommerce.entity.Supplier;
import com.example.agriecommerce.entity.SupplierBankInfo;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.PasswordDto;
import com.example.agriecommerce.payload.SupplierDto;
import com.example.agriecommerce.repository.BankInfoRepository;
import com.example.agriecommerce.repository.ImageRepository;
import com.example.agriecommerce.repository.SupplierRepository;
import com.example.agriecommerce.service.CloudinaryService;
import com.example.agriecommerce.service.ImageService;
import com.example.agriecommerce.service.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {
    private SupplierRepository supplierRepository;
    private BankInfoRepository bankInfoRepository;
    private ModelMapper modelMapper;
    private CloudinaryService cloudinaryService;
    private ImageRepository imageRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               BankInfoRepository bankInfoRepository,
                               ModelMapper modelMapper,
                               CloudinaryService cloudinaryService,
                               ImageRepository imageRepository,
                               PasswordEncoder passwordEncoder) {
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
        this.passwordEncoder = passwordEncoder;
        this.bankInfoRepository = bankInfoRepository;
    }

    @Override
    public SupplierDto getSupplierById(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        return modelMapper.map(supplier, SupplierDto.class);
    }

    @Override
    public List<SupplierDto> getAllSupplier() {
        List<Supplier> suppliers = supplierRepository.findAll();
        return suppliers.stream()
                .map(supplier -> modelMapper.map(supplier, SupplierDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDto updateAccountInfo(Long supplierId, SupplierDto supplierDto) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        supplier.setId(supplierId);

        supplier.setContactName(supplierDto.getContactName());
        supplier.setPhone(supplierDto.getPhone());

        Supplier updatedSupplier = supplierRepository.save(supplier);

        return modelMapper.map(updatedSupplier, SupplierDto.class);
    }

    @Override
    public SupplierDto updateGeneralInfo(Long supplierId, SupplierDto supplierDto) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );

        supplier.setId(supplierId);
        supplier.setContactName(supplierDto.getContactName());
        supplier.setShopName(supplierDto.getShopName());
        supplier.setCccd(supplierDto.getCccd());
        supplier.setEmail(supplierDto.getEmail());
        supplier.setPhone(supplierDto.getPhone());
        supplier.setTax_number(supplierDto.getTax_number());
        supplier.setAddress(supplierDto.getAddress());

        Supplier updatedSupplier = supplierRepository.save(supplier);

        return modelMapper.map(updatedSupplier, SupplierDto.class);
    }

    @Override
    @Transactional
    public SupplierDto updateBankInfo(Long supplierId, SupplierDto supplierDto) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );

        supplier.setId(supplierId);
        supplier.setSellerType(supplierDto.getSellerType());

        Long bankInfoId = supplier.getBankInfo().getId();
        SupplierBankInfo supplierBankInfo = bankInfoRepository.findById(bankInfoId).orElseThrow(
                () -> new ResourceNotFoundException("bankInfo", "id", bankInfoId)
        );

        supplierBankInfo.setId(bankInfoId);
        supplierBankInfo.setBankAccountNumber(supplierDto.getBankAccountNumber());
        supplierBankInfo.setBankName(supplierDto.getBankName());
        supplierBankInfo.setBankBranchName(supplierDto.getBankBranchName());
        supplierBankInfo.setAccountOwner(supplierDto.getBankAccountOwner());

        bankInfoRepository.save(supplierBankInfo);
        Supplier updatedSupplier = supplierRepository.save(supplier);

        return modelMapper.map(updatedSupplier, SupplierDto.class);
    }

    @Override
    public SupplierDto updateSupplierAvatar(Long supplierId, MultipartFile file) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        supplier.setId(supplierId);

        Map result = cloudinaryService.upload(file);
        // save to images table
        Image image = new Image((String) result.get("original_filename"),
                (String) result.get("url"),
                (String) result.get("public_id"));
        imageRepository.save(image);

        if (!Objects.isNull(supplier.getAvatar())) {
            String imageUrl = supplier.getAvatar();
            Image oldImage = imageRepository.findByImageUrl(imageUrl).orElseThrow(
                    () -> new ResourceNotFoundException("Image does not exists")
            );

            String cloudinaryImageId = oldImage.getImageId();
            cloudinaryService.delete(cloudinaryImageId);
            imageRepository.delete(oldImage);
        }

        supplier.setAvatar((String) result.get("url"));
        Supplier updatedSupplier = supplierRepository.save(supplier);

        return modelMapper.map(updatedSupplier, SupplierDto.class);
    }

    @Override
    public SupplierDto changePassword(Long supplierId, PasswordDto passwordDto) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        supplier.setId(supplierId);

        String currentPass = passwordDto.getCurrentPass();
        if (passwordEncoder.matches(currentPass, supplier.getPassword())) {
            String encryptPass = passwordEncoder.encode(passwordDto.getNewPass());
            supplier.setPassword(encryptPass);
        } else
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Password does not match");

        Supplier updatedSupplier = supplierRepository.save(supplier);

        return modelMapper.map(updatedSupplier, SupplierDto.class);
    }
}
