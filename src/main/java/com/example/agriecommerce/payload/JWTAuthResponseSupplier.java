package com.example.agriecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JWTAuthResponseSupplier {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long supplierId;
}
