package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.*;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.repository.BankInfoRepository;
import com.example.agriecommerce.repository.ImageRepository;
import com.example.agriecommerce.repository.SupplierRepository;
import com.example.agriecommerce.service.CloudinaryService;
import com.example.agriecommerce.service.SupplierService;
import com.example.agriecommerce.utils.Encryption;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final SupplierRepository supplierRepository;
    private final BankInfoRepository bankInfoRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final Encryption encryption;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               BankInfoRepository bankInfoRepository,
                               ModelMapper modelMapper,
                               CloudinaryService cloudinaryService,
                               ImageRepository imageRepository,
                               PasswordEncoder passwordEncoder,
                               Encryption encryption) {
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
        this.passwordEncoder = passwordEncoder;
        this.bankInfoRepository = bankInfoRepository;
        this.encryption = encryption;
    }

    @Override
    public SupplierDto getSupplierById(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );

        String secretKey = supplier.getAesKey();
        String iv = supplier.getIv();

        SupplierDto supplierDto = modelMapper.map(supplier, SupplierDto.class);

        return encryption.encryptSupplierDto(supplierDto, secretKey, iv);
    }

    @Override
    public SupplierDto getSupplierInfoById(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        return modelMapper.map(supplier, SupplierDto.class);
    }

    @Override
    public SupplierResponse getAllSuppliers(boolean status, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Supplier> supplierPage = supplierRepository.findByIsActive(status, pageable).orElseThrow(
                () -> new ResourceNotFoundException("user list is empty")
        );

        List<Supplier> supplierList = supplierPage.getContent();
        List<SupplierDto> content = supplierList.stream().map(supplier -> modelMapper.map(supplier, SupplierDto.class)).toList();

        SupplierResponse supplierResponse = new SupplierResponse();
        supplierResponse.setContent(content);
        supplierResponse.setPageNo(supplierPage.getNumber());
        supplierResponse.setPageSize(supplierPage.getSize());
        supplierResponse.setTotalElements(supplierPage.getTotalElements());
        supplierResponse.setTotalPage(supplierPage.getTotalPages());
        supplierResponse.setLast(supplierPage.isLast());

        return supplierResponse;
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

        String secretKey = supplier.getAesKey();
        String iv = supplier.getIv();
        SupplierDto decryptedDto = encryption.decryptSupplierDto(supplierDto, secretKey, iv);

        supplier.setId(supplierId);
        supplier.setContactName(decryptedDto.getContactName());
        supplier.setShopName(decryptedDto.getShopName());
        supplier.setCccd(decryptedDto.getCccd());
        supplier.setEmail(decryptedDto.getEmail());
        supplier.setPhone(decryptedDto.getPhone());
        supplier.setTax_number(decryptedDto.getTax_number());
        supplier.setAddress(decryptedDto.getAddress());

        Supplier updatedSupplier = supplierRepository.save(supplier);

        SupplierDto dto = modelMapper.map(updatedSupplier, SupplierDto.class);

        return encryption.encryptSupplierDto(dto, secretKey, iv);
    }

    @Override
    @Transactional
    public SupplierDto updateBankInfo(Long supplierId, SupplierDto supplierDto) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );

        String secretKey = supplier.getAesKey();
        String iv = supplier.getIv();
        SupplierDto decryptedDto = encryption.decryptSupplierDto(supplierDto, secretKey, iv);

        supplier.setId(supplierId);
        supplier.setSellerType(decryptedDto.getSellerType());

        Long bankInfoId = supplier.getBankInfo().getId();
        SupplierBankInfo supplierBankInfo = bankInfoRepository.findById(bankInfoId).orElseThrow(
                () -> new ResourceNotFoundException("bankInfo", "id", bankInfoId)
        );

        supplierBankInfo.setId(bankInfoId);
        supplierBankInfo.setBankAccountNumber(decryptedDto.getBankAccountNumber());
        supplierBankInfo.setBankName(decryptedDto.getBankName());
        supplierBankInfo.setBankBranchName(decryptedDto.getBankBranchName());
        supplierBankInfo.setAccountOwner(decryptedDto.getBankAccountOwner());

        bankInfoRepository.save(supplierBankInfo);
        Supplier updatedSupplier = supplierRepository.save(supplier);

        SupplierDto dto = modelMapper.map(updatedSupplier, SupplierDto.class);

        return encryption.encryptSupplierDto(dto, secretKey, iv);
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

    @Override
    public ImageDto getSupplierAvatar(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("supplier", "id", supplierId)
        );
        ImageDto imageDto = new ImageDto();
        imageDto.setImageUrl(supplier.getAvatar());

        return imageDto;
    }

    @Override
    public Long getSupplierIdByEmail(String email) {
        Supplier supplier = supplierRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Supplier does not exists")
        );
        return supplier.getId();
    }

    @Override
    public Boolean checkAccountStatus(String email) {
        Supplier supplier = supplierRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Supplier does not exists")
        );
        return supplier.isActive();
    }

    @Override
    public ResultDto updateRSAKey(Long supplierId, AESDto dto) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("Supplier does not exists")
        );

        supplier.setPublicKey(dto.getRsaPublicKey());
        supplierRepository.save(supplier);

        return new ResultDto(true, "Update public key successfully");
    }

    @Override
    public AESDto getRSAPubKey(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("Supplier does not exists")
        );
        AESDto aesDto = new AESDto();
        aesDto.setRsaPublicKey(supplier.getPublicKey());
        return aesDto;
    }

    @Override
    public SupplierDto updateFcmToken(Long supplierId, String fcmToken) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("Supplier does not exists")
        );

        supplier.setId(supplierId);
        supplier.setFcmToken(fcmToken);
        Supplier updatedSupplier = supplierRepository.save(supplier);

        return modelMapper.map(updatedSupplier, SupplierDto.class);
    }

    @Override
    public ResultDto updateAccountStatus(Long supplierId, boolean state) {
        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
                () -> new ResourceNotFoundException("Supplier does not exists")
        );

        supplier.setId(supplierId);
        supplier.setActive(state);
        supplierRepository.save(supplier);

        return new ResultDto(true, "update account status successfully");
    }

    @Override
    public ResultDto countRegisterAccount(int year) {
        long total = supplierRepository.countRegisterAccount(year);
        return new ResultDto(true, total+"");
    }

    @Override
    public ComparationDto getStatisticSupplier(int month, int year) {
        ComparationDto dto = new ComparationDto();
        int previousMonth = month - 1;
        int previousYear = year;
        if (previousMonth == 0){
            previousMonth = 12;
            previousYear = year - 1;
        }
        long current = supplierRepository.countSuppliersByMonthAndYear(month, year);
        long previous = supplierRepository.countSuppliersByMonthAndYear(previousMonth, previousYear);
        long total = supplierRepository.countTotalSupplier(previousYear);

        long gaps = current - previous;
        if (gaps < 0) gaps *=-1;

        dto.setCurrent((double) current);
        dto.setPrevious((double) previous);
        dto.setGaps((double) gaps);
        dto.setTotal((double) total);

        return dto;
    }
}
