package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.LoginDto;
import com.example.agriecommerce.payload.SupplierRegisterDto;
import com.example.agriecommerce.payload.UserRegisterDto;
import com.example.agriecommerce.payload.UserRegisterResponse;

public interface AuthService {
    String login(LoginDto loginDto);
    UserRegisterResponse userRegister(UserRegisterDto userRegisterDto);
    String supplierRegister(SupplierRegisterDto supplierRegisterDto);
}
