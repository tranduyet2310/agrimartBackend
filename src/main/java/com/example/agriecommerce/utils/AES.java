package com.example.agriecommerce.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AES {
    private SecretKey key;
    private final int KEY_SIZE = 128;
    private final int T_LEN = 128;
    private byte[] IV;

    public void init() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(KEY_SIZE);
        key = generator.generateKey();
    }

    public void initIV() throws Exception {
        Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
        IV = encryptionCipher.getIV();
    }

    public void initFromString(String secretKey, String IV) {
        key = new SecretKeySpec(decode(secretKey), "AES");
        this.IV = decode(IV);
    }

    public String encrypt(String message) throws Exception {
        byte[] messageInBytes = message.getBytes();
        Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");

        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);

        encryptionCipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
        return encode(encryptedBytes);
    }

    public String encryptOrginal(String message) throws Exception {
        byte[] messageInBytes = message.getBytes();
        Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, key);
        IV = encryptionCipher.getIV();
        byte[] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
        return encode(encryptedBytes);
    }

    public String decrypt(String encryptedMessage) throws Exception {
        byte[] messageInBytes = decode(encryptedMessage);
        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
        decryptionCipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public String exportKeys() {
        return encode(key.getEncoded());
    }

    public String exportIV() {
        return encode(IV);
    }

    private String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public static void main(String[] args) {
        try {
            AES aes = new AES();
            aes.initFromString("xXpDxebri8xR97YEaZjlrw==", "XXswG/XU/c6oEZLM");
            String encrypt = aes.encrypt("Táo đỏ");
            String decrypt = aes.decrypt(encrypt);
//hKxt2XLDMJHaCmBlc3c3aUyrPWgMZAExqw==
//            KKuCk6xh5fFmf4nMI/LbG+WojBM=
// uwej2DUnxREU4n66aMVuHnGXjXOXXozts6Q+NnRxtg==
            aes.decode("uwej2DUnxREU4n66aMVuHnGXjXOXXozts6Q+NnRxtg==");

            System.out.println();

            System.out.println(encrypt);
            System.out.println(decrypt);

//            aes.exportKeys();

//            aes.init();
//            aes.initIV();
//            System.out.println("key " + aes.exportKeys());
//            System.out.println("iv " + aes.exportIV());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
