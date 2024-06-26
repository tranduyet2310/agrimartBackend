package com.example.agriecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "contact_name", nullable = false)
    private String contactName;
    @Column(name = "shop_name", nullable = false)
    private String shopName;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String cccd;
    @Column(nullable = false)
    private String tax_number;
    private String address;
    @Column(nullable = false)
    private String province;
    @Column(nullable = false)
    private String sellerType;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String avatar;
    @Column(name = "public_key")
    private String publicKey;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_info_id", referencedColumnName = "id")
    private SupplierBankInfo bankInfo;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role roles;
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Warehouse> warehouses;

    @Column(name = "aes_key")
    private String aesKey;
    @Column(name = "iv")
    private String iv;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "fcm")
    private String fcmToken;
    @CreationTimestamp
    private LocalDateTime dateCreated;
}
