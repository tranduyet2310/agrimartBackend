package com.example.agriecommerce.payload;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDto {
    private Long id;
    private String contactName;
    private String shopName;
    private String email;
    private String phone;
    private String cccd;
    private String tax_number;
    private String address;
    private String province;
    private String password;
    private String sellerType;
    private String bankAccountNumber;
    private String bankAccountOwner;
    private String bankName;
    private String bankBranchName;
    private String avatar;
    private boolean isActive;
    private String fcmToken;
}
