package com.example.agriecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressDto {
    private Long id;
    private String contactName;
    private String phone;
    private String province;
    private String district;
    private String commune;
    private String details;
    private String userFullName;
}
