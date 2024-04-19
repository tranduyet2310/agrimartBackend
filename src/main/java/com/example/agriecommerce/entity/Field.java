package com.example.agriecommerce.entity;

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
@Entity
@Table(name = "tbl_field")
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String season;
    private String cropsName;
    private String cropsType;
    private String area;
    private Double estimateYield;
    private Long estimatePrice;
    private Boolean isComplete;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "field", cascade = CascadeType.ALL)
    private List<FieldDetail> fieldDetails;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Supplier supplier;
}
