package com.example.agriecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String password;
    private String avatar;
    private boolean status;
    private String fcmToken;
}
