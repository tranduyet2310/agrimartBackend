package com.example.agriecommerce.service;

import com.example.agriecommerce.payload.*;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserDto getUserInfo(Long userId);
    UserDto updateUserInfo(Long userId, UserDto userDto);
    UserDto updateUserAvatar(Long userId, MultipartFile file);
    UserDto changePassword(Long userId, PasswordDto passwordDto) throws FirebaseAuthException;
    UserDto updateFcmToken(Long userId, String fcmToken);
    UserResponse getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);
    Long getUserIdByEmail(String email);
    Boolean checkAdminRole(String email);
    Boolean checkAccountStatus(String email);
    UserDto updateStatusAccount(Long userId, Integer status);
    ComparationDto countAccountByMonthAndYear(int month, int year);
    List<LineChartAccountDto> getChartData(int month, int year);
    String getUserUidByEmail(String email) throws FirebaseAuthException;
}
