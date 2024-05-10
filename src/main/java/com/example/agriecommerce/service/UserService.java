package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.PasswordDto;
import com.example.agriecommerce.payload.UserDto;
import com.example.agriecommerce.payload.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserDto getUserInfo(Long userId);
    UserDto updateUserInfo(Long userId, UserDto userDto);
    UserDto updateUserAvatar(Long userId, MultipartFile file);
    UserDto changePassword(Long userId, PasswordDto passwordDto);
    UserDto updateFcmToken(Long userId, String fcmToken);
    UserResponse getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);
    Long getUserIdByEmail(String email);
    Boolean checkAdminRole(String email);
}
