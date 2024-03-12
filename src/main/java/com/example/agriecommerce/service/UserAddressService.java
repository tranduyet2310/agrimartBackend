package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.UserAddressDto;

import java.util.List;

public interface UserAddressService {
    UserAddressDto createAddress(Long userId, UserAddressDto userAddressDto);
    UserAddressDto getAddressById(Long addressId);
    List<UserAddressDto> getAllAddressByUserId(Long userId);
    void deleteAddress(Long userId, Long addressId);
    UserAddressDto updateAddress(Long userId, Long addressId, UserAddressDto userAddressDto);
}
