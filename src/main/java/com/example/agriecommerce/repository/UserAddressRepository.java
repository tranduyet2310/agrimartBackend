package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    Optional<List<UserAddress>> findByUserId(Long id);
    Integer countByUserId(Long userId);
}
