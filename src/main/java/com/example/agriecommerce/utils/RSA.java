package com.example.agriecommerce.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public RSA(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String encryptWithDestinationKey(String rsaPublicKey, String message) throws Exception {
        byte[] publicKeyInBytes = decode(rsaPublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyInBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey resultRSAKey = keyFactory.generatePublic(keySpec);

        byte[] messageToBytes = message.getBytes();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, resultRSAKey);
        byte[] encryptedBytes = cipher.doFinal(messageToBytes);
        return encode(encryptedBytes);
    }

    public String encrypt(String message) throws Exception {
        byte[] messageToBytes = message.getBytes();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(messageToBytes);
        return encode(encryptedBytes);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
        return new String(decryptedMessage, StandardCharsets.UTF_8);
    }

    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public String privateKey() {
        return encode(privateKey.getEncoded());
    }

    public String publicKey() {
        return encode(publicKey.getEncoded());
    }
}
