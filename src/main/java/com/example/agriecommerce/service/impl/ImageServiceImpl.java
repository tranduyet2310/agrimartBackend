package com.example.agriecommerce.service.impl;

import com.example.agriecommerce.entity.Image;
import com.example.agriecommerce.exception.ResourceNotFoundException;
import com.example.agriecommerce.repository.ImageRepository;
import com.example.agriecommerce.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {
    @Autowired
    ImageRepository imageRepository;

    public List<Image> list() {
        return imageRepository.findByOrderById();
    }

    public Optional<Image> getOne(int id) {
        return imageRepository.findById(id);
    }

    public void save(Image image) {
        imageRepository.save(image);
    }

    public void delete(int id) {
        imageRepository.deleteById(id);
    }

    public boolean exists(int id) {
        return imageRepository.existsById(id);
    }

    @Override
    public Image findImage(String publicId) {
        Image image = imageRepository.findByImageId(publicId).orElseThrow(
                () -> new ResourceNotFoundException("Image does not exists in db")
        );
        return image;
    }

    @Override
    public Image getImageByImageUrl(String imageUrl) {
        Image image = imageRepository.findByImageUrl(imageUrl).orElseThrow(
                () -> new ResourceNotFoundException("Image does not exists in db")
        );
        return image;
    }
}
