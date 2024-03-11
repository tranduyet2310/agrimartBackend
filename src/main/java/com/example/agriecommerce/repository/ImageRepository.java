package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByOrderById();
    Optional<Image> findByImageId(String imageId);
    Optional<Image> findByImageUrl(String imageUrl);
}
