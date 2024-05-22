package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Product;
import com.example.agriecommerce.entity.Role;
import com.example.agriecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<Page<User>> findByRoles(Role role, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM tbl_user u WHERE MONTH(u.date_created) = :month AND YEAR(u.date_created) = :year")
    long countUsersByMonthAndYear(@Param("month") int month, @Param("year") int year);
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM tbl_user")
    long countTotalUser();
}
