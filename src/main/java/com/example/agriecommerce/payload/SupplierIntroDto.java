package com.example.agriecommerce.payload;

import com.example.agriecommerce.entity.Image;
import com.example.agriecommerce.entity.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierIntroDto {
    private Long id;
    private String description;
    private String type;
    private Long supplierId;
    private List<ImageDto> images;
}
