package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Image;
import com.example.agriecommerce.entity.Product;
import com.example.agriecommerce.entity.Role;
import com.example.agriecommerce.entity.User;
import com.example.agriecommerce.exception.AgriMartException;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.payload.*;
import com.example.agriecommerce.repository.ImageRepository;
import com.example.agriecommerce.repository.RoleRepository;
import com.example.agriecommerce.repository.SupplierRepository;
import com.example.agriecommerce.repository.UserRepository;
import com.example.agriecommerce.service.CloudinaryService;
import com.example.agriecommerce.service.UserService;
import com.example.agriecommerce.utils.AppConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final SupplierRepository supplierRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper,
                           CloudinaryService cloudinaryService,
                           ImageRepository imageRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           SupplierRepository supplierRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public UserDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserResponse getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(
                () -> new ResourceNotFoundException("ROLE_USER does not exsist")
        );
        Page<User> userPage = userRepository.findByRoles(role, pageable).orElseThrow(
                () -> new ResourceNotFoundException("user list is empty")
        );

        List<User> userList = userPage.getContent();
        List<UserDto> content = userList.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();

        UserResponse userResponse = new UserResponse();
        userResponse.setContent(content);
        userResponse.setPageNo(userPage.getNumber());
        userResponse.setPageSize(userPage.getSize());
        userResponse.setTotalElements(userPage.getTotalElements());
        userResponse.setTotalPage(userPage.getTotalPages());
        userResponse.setLast(userPage.isLast());

        return userResponse;
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
                (String) result.get("secure_url"),
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

        user.setAvatar((String) result.get("secure_url"));
        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto changePassword(Long userId, PasswordDto passwordDto) throws FirebaseAuthException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );

        user.setId(userId);
        String currentPass = passwordDto.getCurrentPass();

        // Firebase
        String userUid = getUserUidByEmail(user.getEmail());
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(userUid)
                .setPassword(passwordDto.getNewPass());
        FirebaseAuth.getInstance().updateUser(request);

        if (passwordEncoder.matches(currentPass, user.getPassword())) {
            String encryptPass = passwordEncoder.encode(passwordDto.getNewPass());
            user.setPassword(encryptPass);
        } else
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Password does not match");
        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto updateFcmToken(Long userId, String fcmToken) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );

        user.setId(userId);
        user.setFcmToken(fcmToken);
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

    @Override
    public Boolean checkAdminRole(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email does not exists in DB")
        );
        Role role = user.getRoles();
        return role.getName().equals("ROLE_ADMIN");
    }

    @Override
    public Boolean checkAccountStatus(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email does not exists in DB")
        );
        return user.getStatus() == 1;
    }

    @Override
    public UserDto updateStatusAccount(Long userId, Integer status) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User does not exists in DB")
        );
        user.setStatus(status);
        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public ComparationDto countAccountByMonthAndYear(int month, int year) {
        ComparationDto dto = new ComparationDto();
        int previousMonth = month - 1;
        int previousYear = year;
        if (previousMonth == 0){
            previousMonth = 12;
            previousYear = year - 1;
        }
        long current = userRepository.countUsersByMonthAndYear(month, year);
        long previous = userRepository.countUsersByMonthAndYear(previousMonth, previousYear);
        long total = userRepository.countTotalUser(previousYear);
        long gaps = current - previous;
        if (gaps < 0) gaps *=-1;

        dto.setCurrent((double) current);
        dto.setPrevious((double) previous);
        dto.setGaps((double) gaps);
        dto.setTotal((double) total);

        return dto;
    }

    @Override
    public List<LineChartAccountDto> getChartData(int month, int year) {
        List<LineChartAccountDto> dataSource = new ArrayList<>();
        if (month <= 0 || month > 12)
            throw new AgriMartException(HttpStatus.BAD_REQUEST, "Month is invalid, m=" + month);

        for (int i = 1; i <= 12; i++) {
            LineChartAccountDto item = new LineChartAccountDto();
            long user = userRepository.countUsersByMonthAndYear(i, year);
            long supplier = supplierRepository.countSuppliersByMonthAndYear(i, year);
            String name = AppConstants.listMonths[i - 1];

            item.setName(name);
            item.setUser((double) user);
            item.setSupplier((double) supplier);

            dataSource.add(item);
        }

        return dataSource;
    }

    @Override
    public String getUserUidByEmail(String email) throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
        return userRecord.getUid();
    }
}
