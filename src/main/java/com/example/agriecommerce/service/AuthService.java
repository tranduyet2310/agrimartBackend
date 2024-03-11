package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.LoginDto;
import com.example.agriecommerce.payload.SupplierRegisterDto;
import com.example.agriecommerce.payload.UserRegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String userRegister(UserRegisterDto userRegisterDto);
    String supplierRegister(SupplierRegisterDto supplierRegisterDto);
}
