package com.example.agriecommerce;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
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
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Boot REST API Documentation",
                description = "API Documenation for Agrimart",
                version = "v1.0",
                contact = @Contact(
                        name = "Duyet",
                        email = "trantheduyetsl@gmail.com",
                        url = "https://github.com/tranduyet2310"
                )
        )
)
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
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp){
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                new ClassPathResource("firebase-service-account.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials).build();
        return FirebaseApp.initializeApp(firebaseOptions);
    }

    public static void main(String[] args) {
        SpringApplication.run(AgriecommerceApplication.class, args);
    }

}
