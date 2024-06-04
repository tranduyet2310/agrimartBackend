package com.example.agriecommerce.controller;

import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.service.UserService;
import com.example.agriecommerce.utils.AppConstants;
import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Tag(name = "REST APIs for User")
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
    @PreAuthorize(("hasRole('USER') or hasRole('ADMIN')"))
    public ResponseEntity<UserDto> updateUserInfo(@PathVariable("id") Long userId,
                                                  @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUserInfo(userId, userDto));
    }

    @PatchMapping("{id}/status")
    @PreAuthorize(("hasRole('ADMIN')"))
    public ResponseEntity<UserDto> updateAccountStatus(@PathVariable("id") Long userId,
                                                       @RequestParam("status") Integer status) {
        return ResponseEntity.ok(userService.updateStatusAccount(userId, status));
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
        try {
            return ResponseEntity.ok(userService.changePassword(userId, passwordDto));
        } catch (FirebaseAuthException e) {
            throw new AgriMartException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("{id}/fcm")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<UserDto> updateFcmToken(@PathVariable("id") Long userId,
                                                  @RequestParam("token") String fcmToken) {
        return ResponseEntity.ok(userService.updateFcmToken(userId, fcmToken));
    }

    @GetMapping("statistic")
    public ResponseEntity<ComparationDto> getStatistic(@RequestParam("m") int month,
                                                       @RequestParam("y") int year) {
        return ResponseEntity.ok(userService.countAccountByMonthAndYear(month, year));
    }

    @GetMapping("chart")
    public ResponseEntity<List<LineChartAccountDto>> getChartData(@RequestParam("m") int month,
                                                                  @RequestParam("y") int year) {
        return ResponseEntity.ok(userService.getChartData(month, year));
    }
}
