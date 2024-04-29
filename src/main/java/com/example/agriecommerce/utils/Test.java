package com.example.agriecommerce.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class Test {
    private static final String SIGNING_ALGORITHM = "SHA256withRSA";

    public static byte[] createDigitalSignature(
            byte[] input,
            PrivateKey Key)
            throws Exception
    {
        Signature signature = Signature.getInstance(SIGNING_ALGORITHM);
        signature.initSign(Key);
        signature.update(input);
        return signature.sign();
    }

    public static boolean verifyDigitalSignature(
            byte[] input,
            byte[] signatureToVerify,
            PublicKey key)
            throws Exception
    {
        Signature signature = Signature.getInstance(SIGNING_ALGORITHM);
        signature.initVerify(key);
        signature.update(input);
        return signature.verify(signatureToVerify);
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash)
    {
        BigInteger number = new BigInteger(1, hash);

        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

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

            String message = "Hello world";
            // step 1: hash source
            String hashMessage = toHexString(getSHA(message));
            // step 2: create signature from source
            byte[] digitalSignature = createDigitalSignature(hashMessage.getBytes(), privateKey);
            // step 3: encrypt

            String result = "hello world";
            String desMessage = toHexString(getSHA(result));
            if (verifyDigitalSignature(desMessage.getBytes(), digitalSignature, publicKey)){
                System.out.println("hash "+hashMessage);
                System.out.println("Receive successfully");
            }


//            String encryptedMessage = rsa.encryptWithDestinationKey(destinationKey, "Hello");
//            System.out.println("message: "+message);
//            System.out.println("result: "+rsa.decrypt(encryptedMessage));

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
