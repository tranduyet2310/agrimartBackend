package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Product;
import com.example.agriecommerce.entity.Role;
import com.example.agriecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<Page<User>> findByRoles(Role role, Pageable pageable);
}
