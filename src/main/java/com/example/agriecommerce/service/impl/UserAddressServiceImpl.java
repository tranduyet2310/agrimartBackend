package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.User;
import com.example.agriecommerce.entity.UserAddress;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.ResultDto;
import com.example.agriecommerce.payload.UserAddressDto;
import com.example.agriecommerce.repository.UserAddressRepository;
import com.example.agriecommerce.repository.UserRepository;
import com.example.agriecommerce.service.UserAddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.agriecommerce.utils.AppConstants.MAX_ADDRESS;

@Service
public class UserAddressServiceImpl implements UserAddressService {
    private final UserAddressRepository userAddressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserAddressServiceImpl(UserAddressRepository userAddressRepository,
                                  UserRepository userRepository,
                                  ModelMapper modelMapper) {
        this.userAddressRepository = userAddressRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public UserAddressDto createAddress(Long userId, UserAddressDto userAddressDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );

        UserAddress address = new UserAddress();
        address.setUser(user);
        address.setContactName(userAddressDto.getContactName());
        address.setPhone(userAddressDto.getPhone());
        address.setProvince(userAddressDto.getProvince());
        address.setDistrict(userAddressDto.getDistrict());
        address.setCommune(userAddressDto.getCommune());
        address.setDetails(userAddressDto.getDetails());

        int numberOfAddress = userAddressRepository.countByUserId(userId);
        if(numberOfAddress >= MAX_ADDRESS)
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "User can only have a maximum of 5 address");

        UserAddress savedAddress = userAddressRepository.save(address);

        List<UserAddress> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        userRepository.save(user);

        return modelMapper.map(savedAddress, UserAddressDto.class);
    }

    @Override
    public UserAddressDto getAddressById(Long addressId) {
        UserAddress address = userAddressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("address", "id", addressId)
        );
        return modelMapper.map(address, UserAddressDto.class);
    }

    @Override
    public List<UserAddressDto> getAllAddressByUserId(Long userId) {
        List<UserAddress> addressList = userAddressRepository.findByUserId(userId).orElseThrow(
                () -> new ResourceNotFoundException("addresses does not exists with id" + userId)
        );
        return addressList.stream()
                .map(userAddress -> modelMapper.map(userAddress, UserAddressDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResultDto deleteAddress(Long userId, Long addressId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );
        UserAddress address = userAddressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("address", "id", addressId)
        );

        userAddressRepository.delete(address);
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccessful(true);
        resultDto.setMessage("Delete user address successfully");
        return resultDto;
    }

    @Override
    public UserAddressDto updateAddress(Long userId, Long addressId, UserAddressDto userAddressDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );
        UserAddress address = userAddressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("address", "id", addressId)
        );

        address.setId(addressId);
        address.setContactName(userAddressDto.getContactName());
        address.setPhone(userAddressDto.getPhone());
        address.setProvince(userAddressDto.getProvince());
        address.setDistrict(userAddressDto.getDistrict());
        address.setCommune(userAddressDto.getCommune());
        address.setDetails(userAddressDto.getDetails());
        address.setUser(user);

        UserAddress updatedAddress = userAddressRepository.save(address);

        return modelMapper.map(updatedAddress, UserAddressDto.class);
    }
}
