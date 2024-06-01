package com.example.agriecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDto {
    private Long id;
    private String warehouseName;
    private String contact;
    private String phone;
    private String email;
    private String province;
    private String district;
    private String commune;
    private String detail;
    private String supplierContactName;
    private boolean isActive;
}
