package com.example.agriecommerce.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
    Map upload(MultipartFile multipartFile);
    Map delete(String id);
}
