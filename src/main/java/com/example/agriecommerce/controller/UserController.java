package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.PasswordDto;
import com.example.agriecommerce.payload.UserDto;
import com.example.agriecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable("id") Long userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PatchMapping("{id}")
    public ResponseEntity<UserDto> updateUserInfo(@PathVariable("id") Long userId,
                                                  @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUserInfo(userId, userDto));
    }

    @PatchMapping("{id}/avatar")
    public ResponseEntity<UserDto> updateUserAvatar(@PathVariable("id") Long userId,
                                                    @RequestParam("file") MultipartFile multipartFile) {
        return ResponseEntity.ok(userService.updateUserAvatar(userId, multipartFile));
    }

    @PatchMapping("{id}/password")
    public ResponseEntity<UserDto> changePassword(@PathVariable("id") Long userId,
                                                  @RequestBody PasswordDto passwordDto) {
        return ResponseEntity.ok(userService.changePassword(userId, passwordDto));
    }
}
