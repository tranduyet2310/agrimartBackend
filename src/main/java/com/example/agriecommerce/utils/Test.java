package com.example.agriecommerce.utils;

import com.example.agriecommerce.config.RSAConfig;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Test {

    public static void main(String[] args) {

//        RSAConfig rsaConfig = new RSAConfig();
//        RSA rsa = rsaConfig.rsa();
        PrivateKey privateKey;
        PublicKey publicKey;
        try {
//            String s = rsa.encrypt("hello");
//            String r = rsa.decrypt(s);
//            System.out.println(s);
//            System.out.println(r);
//            System.out.println("private: "+rsa.privateKey());
//            System.out.println("public: "+rsa.publicKey());

            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair keyPair = generator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

            System.out.println("private: "+Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            System.out.println("public: "+Base64.getEncoder().encodeToString(publicKey.getEncoded()));

            System.out.println("\n");

            String destinationKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

            RSA rsa = new RSA(privateKey, publicKey);
            String message = rsa.encryptWithDestinationKey(destinationKey, "Hello");
            System.out.println("message: "+message);
            System.out.println("result: "+rsa.decrypt(message));

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
