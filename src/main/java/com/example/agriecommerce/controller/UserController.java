package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.PasswordDto;
import com.example.agriecommerce.payload.UserDto;
import com.example.agriecommerce.payload.UserResponse;
import com.example.agriecommerce.service.UserService;
import com.example.agriecommerce.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<UserDto> getUserInfo(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getAllUser(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(userService.getAllUsers(pageNo, pageSize, sortBy, sortDir));
    }

    @PatchMapping("{id}")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<UserDto> updateUserInfo(@PathVariable("id") Long userId,
                                                  @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUserInfo(userId, userDto));
    }

    @PatchMapping("{id}/avatar")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<UserDto> updateUserAvatar(@PathVariable("id") Long userId,
                                                    @RequestParam("file") MultipartFile multipartFile) {
        return ResponseEntity.ok(userService.updateUserAvatar(userId, multipartFile));
    }

    @PatchMapping("{id}/password")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<UserDto> changePassword(@PathVariable("id") Long userId,
                                                  @RequestBody PasswordDto passwordDto) {
        return ResponseEntity.ok(userService.changePassword(userId, passwordDto));
    }

    @PatchMapping("{id}/fcm")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<UserDto> updateFcmToken(@PathVariable("id") Long userId,
                                                  @RequestParam("token") String fcmToken) {
        return ResponseEntity.ok(userService.updateFcmToken(userId, fcmToken));
    }
}
