package com.example.agriecommerce;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.agriecommerce.utils.AppConstants.*;

@SpringBootApplication
@EnableScheduling
public class AgriecommerceApplication {
    @Bean
    public Cloudinary getCloudinary() {
        return new Cloudinary(ObjectUtils.asMap("cloud_name", CLOUD_NAME, "api_key", CLOUD_API_KEY,
                "api_secret", CLOUD_API_SECRET, "secure", true));
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                new ClassPathResource("firebase-service-account.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials).build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "agrimart");
        return FirebaseMessaging.getInstance(app);
    }

    public static void main(String[] args) {
        SpringApplication.run(AgriecommerceApplication.class, args);
    }

}
