package com.example.agriecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_cooperation")
public class Cooperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String description;
    private String cropsName;
    private Double requireYield;
    private String investment;
    private String contact;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Supplier supplier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", referencedColumnName = "id")
    private Field field;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private OrderStatus cooperationStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private UserAddress address;
    private String paymentStatus;
}
