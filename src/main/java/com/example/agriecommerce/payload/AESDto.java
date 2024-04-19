package com.example.agriecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AESDto {
    private String rsaPublicKey;
    private String aesKey;
    private String iv;
    private String rsaPublicKeyServer;
}
