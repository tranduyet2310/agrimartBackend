package com.example.agriecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String password;
    private String avatar;
    private Integer status;
    @Column(name = "fcm")
    private String fcmToken;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role roles;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> addresses;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();
    @CreationTimestamp
    private LocalDateTime dateCreated;
}
