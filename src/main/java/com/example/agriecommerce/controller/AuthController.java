package com.example.agriecommerce.controller;

import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.service.AuthService;
import com.example.agriecommerce.service.SupplierService;
import com.example.agriecommerce.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "CRUD REST APIs for Login & Register")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final SupplierService supplierService;

    @Autowired
    public AuthController(AuthService authService,
                          UserService userService,
                          SupplierService supplierService) {
        this.authService = authService;
        this.userService = userService;
        this.supplierService = supplierService;
    }

    @PostMapping("login")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();

        Long userId = userService.getUserIdByEmail(loginDto.getEmail());
        Boolean role = userService.checkAdminRole(loginDto.getEmail());
        Boolean status = userService.checkAccountStatus(loginDto.getEmail());

        if (status) {
            jwtAuthResponse.setAccessToken(token);
            jwtAuthResponse.setUserId(userId);
            jwtAuthResponse.setAdmin(role);
        } else {
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Account is banned");
        }

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("login/supplier")
    public ResponseEntity<JWTAuthResponseSupplier> loginSupplier(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        JWTAuthResponseSupplier jwtAuthResponse = new JWTAuthResponseSupplier();

        Long supplierId = supplierService.getSupplierIdByEmail(loginDto.getEmail());
        Boolean status = supplierService.checkAccountStatus(loginDto.getEmail());

        if (status) {
            jwtAuthResponse.setAccessToken(token);
            jwtAuthResponse.setSupplierId(supplierId);
            jwtAuthResponse.setActive(status);
        }

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("keys")
    public ResponseEntity<AESDto> getAESKey(@RequestBody AESDto aesDto) {
        return ResponseEntity.ok(authService.requestAESKey(aesDto));
    }

    @PostMapping("key")
    public ResponseEntity<AESDto> getSessionKey(@RequestBody AESDto aesDto) {
        return ResponseEntity.ok(authService.getSessionKey(aesDto));
    }

    @PostMapping("/register/user")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody UserRegisterDto userRegisterDto) {
        UserRegisterResponse response = authService.userRegister(userRegisterDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/supplier")
    public ResponseEntity<SupplierRegisterResponse> registerSupplier(@RequestBody SupplierRegisterDto supplierRegisterDto) {
        SupplierRegisterResponse response = authService.supplierRegister(supplierRegisterDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
