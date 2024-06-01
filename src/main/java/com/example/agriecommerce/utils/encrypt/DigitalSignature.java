package com.example.agriecommerce.utils.encrypt;

import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

@Component
public class DigitalSignature {
    private static final String SIGNING_ALGORITHM = "SHA256withRSA";

    public byte[] createDigitalSignature(
            byte[] input,
            PrivateKey Key)
            throws Exception
    {
        Signature signature = Signature.getInstance(SIGNING_ALGORITHM);
        signature.initSign(Key);
        signature.update(input);
        return signature.sign();
    }

    public boolean verifyDigitalSignature(
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

}
