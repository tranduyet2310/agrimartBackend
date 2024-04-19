package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Role;
import com.example.agriecommerce.entity.Supplier;
import com.example.agriecommerce.entity.SupplierBankInfo;
import com.example.agriecommerce.entity.User;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.repository.BankInfoRepository;
import com.example.agriecommerce.repository.RoleRepository;
import com.example.agriecommerce.repository.SupplierRepository;
import com.example.agriecommerce.repository.UserRepository;
import com.example.agriecommerce.security.JwtTokenProvider;
import com.example.agriecommerce.service.AuthService;
import com.example.agriecommerce.utils.AES;
import com.example.agriecommerce.utils.RSA;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SupplierRepository supplierRepository;
    private final BankInfoRepository bankInfoRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper modelMapper;
    private final RSA rsa;
    private final AES aes;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           SupplierRepository supplierRepository,
                           BankInfoRepository bankInfoRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           ModelMapper modelMapper,
                           RSA rsa,
                           AES aes) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.bankInfoRepository = bankInfoRepository;
        this.modelMapper = modelMapper;
        this.rsa = rsa;
        this.aes = aes;
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public UserRegisterResponse userRegister(UserRegisterDto userRegisterDto) {
        // add check for email exists in DB
        if (userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Email is already exists");
        }

        User user = new User();
        user.setFullName(userRegisterDto.getFullName());
        user.setPhone(userRegisterDto.getPhone());
        user.setEmail(userRegisterDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));

//        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").get();
//        roles.add(userRole);

//        user.setRoles(roles);
        user.setRoles(userRole);
        user.setStatus(1); // 1 - active; 0 - deleted

        // get public key RSA
        user.setPublicKey(userRegisterDto.getRsaPublicKey());

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserRegisterResponse.class);
    }

    @Override
    @Transactional
    public SupplierRegisterResponse supplierRegister(SupplierRegisterDto supplierRegisterDto) {
        // add check for email exists in DB
        if (supplierRepository.existsByEmail(supplierRegisterDto.getEmail())) {
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Email is already exists");
        }

        String encryptedAESKey = supplierRegisterDto.getAesKey();
        String encryptedIV = supplierRegisterDto.getIv();
        String aesKey, iv;

        try {
            aesKey = rsa.decrypt(encryptedAESKey);
            iv = rsa.decrypt(encryptedIV);
        } catch (Exception e) {
            throw new AgriMartException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to initialize key");
        }

        SupplierRegisterDto decryptDto = decryptSupplierDto(supplierRegisterDto, aesKey, iv);

        Supplier supplier = new Supplier();
        supplier.setContactName(decryptDto.getContactName());
        supplier.setShopName(decryptDto.getShopName());
        supplier.setPhone(decryptDto.getPhone());
        supplier.setCccd(decryptDto.getCccd());
        supplier.setTax_number(decryptDto.getTax_number());
        supplier.setProvince(decryptDto.getProvince());
        supplier.setSellerType(decryptDto.getSellerType());
        supplier.setEmail(decryptDto.getEmail());
        supplier.setPassword(passwordEncoder.encode(decryptDto.getPassword()));
        // address is empty when supplier registers account
        supplier.setAddress("");

//        Set<Role> roles = new HashSet<>();
        Role supplierRole = roleRepository.findByName("ROLE_SUPPLIER").get();
//        roles.add(supplierRole);

        SupplierBankInfo info = new SupplierBankInfo();
        info.setBankAccountNumber(decryptDto.getBankAccountNumber());
        info.setAccountOwner(decryptDto.getBankAccountOwner());
        info.setBankName(decryptDto.getBankName());
        info.setBankBranchName(decryptDto.getBankBranchName());

        bankInfoRepository.save(info);

//        supplier.setRoles(roles);
        supplier.setRoles(supplierRole);
        supplier.setBankInfo(info);
        // get public key RSA
        supplier.setPublicKey(decryptDto.getRsaPublicKey());

        Supplier savedSupplier = supplierRepository.save(supplier);

        return modelMapper.map(savedSupplier, SupplierRegisterResponse.class);
    }

    @Override
    public AESDto requestAESKey(AESDto aesDto) {
        AESDto dto = new AESDto();
        try {
            aes.init();
            aes.initIV();
            String aesKey = aes.exportKeys();
            String iv = aes.exportIV();

            System.out.println("aesKey " + aesKey);
            System.out.println("iv " + iv);

            String clientRSAPubKey = aesDto.getRsaPublicKey();
            String encryptedAESKey = rsa.encryptWithDestinationKey(clientRSAPubKey, aesKey);
            String encryptedIV = rsa.encryptWithDestinationKey(clientRSAPubKey, iv);

            dto.setIv(encryptedIV);
            dto.setAesKey(encryptedAESKey);
            dto.setRsaPublicKeyServer(rsa.publicKey());
            dto.setRsaPublicKey(clientRSAPubKey);
        } catch (Exception e) {
            throw new AgriMartException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create Key");
        }
        return dto;
    }

    @Override
    public AESDto getSessionKey(AESDto aesDto) {
        AESDto dto = new AESDto();
        try {
            aes.init();
            aes.initIV();
            String aesKey = aes.exportKeys();
            String iv = aes.exportIV();

            System.out.println("Session-aesKey " + aesKey);
            System.out.println("Session-iv " + iv);

            Supplier supplier = supplierRepository.findByPublicKey(aesDto.getRsaPublicKey()).orElseThrow(
                    () ->  new AgriMartException(HttpStatus.BAD_REQUEST, "Public key does not exists")
            );

            String clientRSAPubKey = aesDto.getRsaPublicKey();
            String encryptedAESKey = rsa.encryptWithDestinationKey(clientRSAPubKey, aesKey);
            String encryptedIV = rsa.encryptWithDestinationKey(clientRSAPubKey, iv);

            dto.setIv(encryptedIV);
            dto.setAesKey(encryptedAESKey);
            dto.setRsaPublicKeyServer(rsa.publicKey());
            dto.setRsaPublicKey(clientRSAPubKey);

            supplier.setAesKey(aesKey);
            supplier.setIv(iv);

            supplierRepository.save(supplier);

        } catch (Exception e) {
            throw new AgriMartException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create Key");
        }
        return dto;
    }

    private SupplierRegisterDto decryptSupplierDto(SupplierRegisterDto supplierRegisterDto, String aesKey, String iv) {
        SupplierRegisterDto registerDto = new SupplierRegisterDto();

        aes.initFromString(aesKey, iv);
        try {
            registerDto.setContactName(aes.decrypt(supplierRegisterDto.getContactName()));
            registerDto.setShopName(aes.decrypt(supplierRegisterDto.getShopName()));
            registerDto.setEmail(aes.decrypt(supplierRegisterDto.getEmail()));
            registerDto.setPhone(aes.decrypt(supplierRegisterDto.getPhone()));
            registerDto.setCccd(aes.decrypt(supplierRegisterDto.getCccd()));
            registerDto.setTax_number(aes.decrypt(supplierRegisterDto.getTax_number()));
            registerDto.setProvince(aes.decrypt(supplierRegisterDto.getProvince()));
            registerDto.setPassword(aes.decrypt(supplierRegisterDto.getPassword()));
            registerDto.setSellerType(aes.decrypt(supplierRegisterDto.getSellerType()));
            registerDto.setBankAccountNumber(aes.decrypt(supplierRegisterDto.getBankAccountNumber()));
            registerDto.setBankAccountOwner(aes.decrypt(supplierRegisterDto.getBankAccountOwner()));
            registerDto.setBankName(aes.decrypt(supplierRegisterDto.getBankName()));
            registerDto.setBankBranchName(aes.decrypt(supplierRegisterDto.getBankBranchName()));
            registerDto.setRsaPublicKey(supplierRegisterDto.getRsaPublicKey());
        } catch (Exception e) {
            throw new AgriMartException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to decrypt data");
        }
        return registerDto;
    }
}
