package com.example.agriecommerce;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

import static com.example.agriecommerce.utils.AppConstants.*;

@SpringBootApplication
public class AgriecommerceApplication {
    @Bean
    public Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", CLOUD_API_KEY,
                "api_secret", CLOUD_API_SECRET,
                "secure", true));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(AgriecommerceApplication.class, args);
    }

}
