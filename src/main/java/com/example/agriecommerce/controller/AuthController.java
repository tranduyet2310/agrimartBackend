package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.JWTAuthResponse;
import com.example.agriecommerce.payload.LoginDto;
import com.example.agriecommerce.payload.SupplierRegisterDto;
import com.example.agriecommerce.payload.UserRegisterDto;
import com.example.agriecommerce.service.AuthService;
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
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("login")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/register/user")
    public ResponseEntity<String> register(@RequestBody UserRegisterDto userRegisterDto) {
        String response = authService.userRegister(userRegisterDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/supplier")
    public ResponseEntity<String> registerSupplier(@RequestBody SupplierRegisterDto supplierRegisterDto) {
        String response = authService.supplierRegister(supplierRegisterDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
