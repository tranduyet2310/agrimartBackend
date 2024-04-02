package com.example.agriecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRegisterDto {
    private String contactName;
    private String shopName;
    private String email;
    private String phone;
    private String cccd;
    private String tax_number;
    private String province;
    private String password;
    private String sellerType;
    private String bankAccountNumber;
    private String accountOwner;
    private String bankName;
    private String bankBranchName;
    private String rsaPublicKey;
}
