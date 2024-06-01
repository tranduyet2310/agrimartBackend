package com.example.agriecommerce.config;

import com.example.agriecommerce.utils.encrypt.RSA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class RSAConfig {
    @Value("${rsa.private-key}")
    private String privateKeyValue;
    @Value("${rsa.public-key}")
    private String publicKeyValue;

    @Bean
    public RSA rsa() {
        try {
            PrivateKey privateKey = getPrivateKey();
            PublicKey publicKey = getPublicKey();
            return new RSA(privateKey, publicKey);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public PrivateKey getPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyValue);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public PublicKey getPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyValue);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}
