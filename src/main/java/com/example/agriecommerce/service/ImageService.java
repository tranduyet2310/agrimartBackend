package com.example.agriecommerce.service;

import com.example.agriecommerce.entity.Image;

import java.util.List;
import java.util.Optional;

public interface ImageService {
    List<Image> list();

    Optional<Image> getOne(int id);

    void save(Image image);

    void delete(int id);

    boolean exists(int id);
}
