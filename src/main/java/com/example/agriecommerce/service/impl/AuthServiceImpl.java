package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Role;
import com.example.agriecommerce.entity.Supplier;
import com.example.agriecommerce.entity.SupplierBankInfo;
import com.example.agriecommerce.entity.User;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.payload.LoginDto;
import com.example.agriecommerce.payload.SupplierRegisterDto;
import com.example.agriecommerce.payload.UserRegisterDto;
import com.example.agriecommerce.payload.UserRegisterResponse;
import com.example.agriecommerce.repository.BankInfoRepository;
import com.example.agriecommerce.repository.RoleRepository;
import com.example.agriecommerce.repository.SupplierRepository;
import com.example.agriecommerce.repository.UserRepository;
import com.example.agriecommerce.security.JwtTokenProvider;
import com.example.agriecommerce.service.AuthService;
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

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private SupplierRepository supplierRepository;
    private BankInfoRepository bankInfoRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private ModelMapper modelMapper;
    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           SupplierRepository supplierRepository,
                           BankInfoRepository bankInfoRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.supplierRepository = supplierRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.bankInfoRepository = bankInfoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

    @Override
    public UserRegisterResponse userRegister(UserRegisterDto userRegisterDto) {
        // add check for email exists in DB
        if(userRepository.existsByEmail(userRegisterDto.getEmail())){
            throw  new AgriMartException(HttpStatus.BAD_REQUEST, "Email is already exists");
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
    public String supplierRegister(SupplierRegisterDto supplierRegisterDto) {
        // add check for email exists in DB
        if(supplierRepository.existsByEmail(supplierRegisterDto.getEmail())){
            throw  new AgriMartException(HttpStatus.BAD_REQUEST, "Email is already exists");
        }

        Supplier supplier = new Supplier();
        supplier.setContactName(supplierRegisterDto.getContactName());
        supplier.setShopName(supplierRegisterDto.getShopName());
        supplier.setPhone(supplierRegisterDto.getPhone());
        supplier.setCccd(supplierRegisterDto.getCccd());
        supplier.setTax_number(supplierRegisterDto.getTax_number());
        supplier.setProvince(supplierRegisterDto.getProvince());
        supplier.setSellerType(supplierRegisterDto.getSellerType());
        supplier.setEmail(supplierRegisterDto.getEmail());
        supplier.setPassword(passwordEncoder.encode(supplierRegisterDto.getPassword()));
        // address is empty when supplier registers account
        supplier.setAddress("");

//        Set<Role> roles = new HashSet<>();
        Role supplierRole = roleRepository.findByName("ROLE_SUPPLIER").get();
//        roles.add(supplierRole);

        SupplierBankInfo info = new SupplierBankInfo();
        info.setBankAccountNumber(supplierRegisterDto.getBankAccountNumber());
        info.setAccountOwner(supplierRegisterDto.getAccountOwner());
        info.setBankName(supplierRegisterDto.getBankName());
        info.setBankBranchName(supplierRegisterDto.getBankBranchName());

        bankInfoRepository.save(info);

//        supplier.setRoles(roles);
        supplier.setRoles(supplierRole);
        supplier.setBankInfo(info);
        // get public key RSA
        supplier.setPublicKey(supplierRegisterDto.getRsaPublicKey());

        supplierRepository.save(supplier);

        return "Supplier registered successfully";
    }
}
