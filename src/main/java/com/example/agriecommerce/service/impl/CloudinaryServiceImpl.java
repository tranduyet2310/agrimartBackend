package com.example.agriecommerce.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.agriecommerce.exception.CloudinaryException;
import com.example.agriecommerce.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    @Autowired
    private Cloudinary cloudinary;

    public Map upload(MultipartFile multipartFile) {
        File file = null;
        Map result;
        try {
            file = convert(multipartFile);
            result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            if (!Files.deleteIfExists(file.toPath())) {
                throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new CloudinaryException("Failed to upload image", e);
        }
        return result;
    }

    public Map delete(String id) {
        Map result;
        try {
            result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new CloudinaryException("Failed to delete image", e);
        }
        return result;
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
}
