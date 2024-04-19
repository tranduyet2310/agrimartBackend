package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.*;

public interface AuthService {
    String login(LoginDto loginDto);
    UserRegisterResponse userRegister(UserRegisterDto userRegisterDto);
    SupplierRegisterResponse supplierRegister(SupplierRegisterDto supplierRegisterDto);
    AESDto requestAESKey(AESDto aesDto);
    AESDto getSessionKey(AESDto aesDto);
}
