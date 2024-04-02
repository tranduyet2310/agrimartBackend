package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Image;
import com.example.agriecommerce.entity.User;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.PasswordDto;
import com.example.agriecommerce.payload.UserDto;
import com.example.agriecommerce.repository.ImageRepository;
import com.example.agriecommerce.repository.UserRepository;
import com.example.agriecommerce.service.CloudinaryService;
import com.example.agriecommerce.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private CloudinaryService cloudinaryService;
    private ImageRepository imageRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           CloudinaryService cloudinaryService,
                           ImageRepository imageRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUserInfo(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );

        user.setId(userId);
        user.setFullName(userDto.getFullName());
        user.setPhone(userDto.getPhone());
//        user.setEmail(userDto.getEmail());

        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto updateUserAvatar(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );

        user.setId(userId);

        Map result = cloudinaryService.upload(file);
        // save to images table
        Image image = new Image((String) result.get("original_filename"),
                (String) result.get("url"),
                (String) result.get("public_id"));
        imageRepository.save(image);

        if (!Objects.isNull(user.getAvatar())) {
            String imageUrl = user.getAvatar();
            Image oldImage = imageRepository.findByImageUrl(imageUrl).orElseThrow(
                    () -> new ResourceNotFoundException("Image does not exists")
            );

            String cloudinaryImageId = oldImage.getImageId();
            cloudinaryService.delete(cloudinaryImageId);
            imageRepository.delete(oldImage);
        }

        user.setAvatar((String) result.get("url"));
        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto changePassword(Long userId, PasswordDto passwordDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );

        user.setId(userId);
        String currentPass = passwordDto.getCurrentPass();
        if (passwordEncoder.matches(currentPass, user.getPassword())) {
            String encryptPass = passwordEncoder.encode(passwordDto.getNewPass());
            user.setPassword(encryptPass);
        } else
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Password does not match");

        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public Long getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email does not exists in DB")
        );
        return user.getId();
    }
}
