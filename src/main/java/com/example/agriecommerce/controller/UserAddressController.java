package com.example.agriecommerce.controller;

import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.payload.UserAddressDto;
import com.example.agriecommerce.service.UserAddressService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "REST APIs for User Address")
@RestController
@RequestMapping("api/users")
public class UserAddressController {
    private final UserAddressService userAddressService;

    @Autowired
    public UserAddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @PostMapping("{userId}/addresses")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<UserAddressDto> createAddress(@PathVariable("userId") Long userId,
                                                        @RequestBody UserAddressDto userAddressDto) {
        UserAddressDto response = userAddressService.createAddress(userId, userAddressDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/addresses/{id}") // both
    public ResponseEntity<UserAddressDto> getAddressById(@PathVariable("id") Long addressId) {
        return ResponseEntity.ok(userAddressService.getAddressById(addressId));
    }

    @GetMapping("{userId}/addresses")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<List<UserAddressDto>> getAddressByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userAddressService.getAllAddressByUserId(userId));
    }

    @PutMapping("{userId}/addresses/{id}")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<UserAddressDto> updateAddress(@PathVariable("userId") Long userId,
                                                        @PathVariable("id") Long addressId,
                                                        @RequestBody UserAddressDto userAddressDto) {
        return ResponseEntity.ok(userAddressService.updateAddress(userId, addressId, userAddressDto));
    }

    @DeleteMapping("{userId}/addresses/{id}")
    @PreAuthorize(("hasRole('USER')"))
    public ResponseEntity<ResultDto> deleteAddress(@PathVariable("userId") Long userId,
                                                   @PathVariable("id") Long addressId){
        ResultDto resultDto = userAddressService.deleteAddress(userId, addressId);
        return ResponseEntity.ok(resultDto);
    }
}
