package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.service.AuthService;
import com.example.agriecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;
    private UserService userService;
    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }
    @PostMapping("login")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        Long userId = userService.getUserIdByEmail(loginDto.getEmail());
        jwtAuthResponse.setUserId(userId);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/register/user")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody UserRegisterDto userRegisterDto) {
        UserRegisterResponse response = authService.userRegister(userRegisterDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/supplier")
    public ResponseEntity<String> registerSupplier(@RequestBody SupplierRegisterDto supplierRegisterDto) {
        String response = authService.supplierRegister(supplierRegisterDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
