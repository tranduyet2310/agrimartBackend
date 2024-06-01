package com.example.agriecommerce.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldDto {
    private Long id;
    private String season;
    private String cropsName;
    private String cropsType;
    private String area;
    private Double estimateYield;
    private List<FieldDetailDto> fieldDetails;
    private Long estimatePrice;
    private Boolean isComplete;
    private Long supplierId;
    private String supplierContactName;
}
