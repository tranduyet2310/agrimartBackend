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
@Table(name = "tbl_bank_info")
public class SupplierBankInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "account_number", nullable = false)
    private String bankAccountNumber;
    @Column(name = "account_owner", nullable = false)
    private String accountOwner;
    @Column(name = "bank_name", nullable = false)
    private String bankName;
    @Column(name = "bank_branch_name", nullable = false)
    private String bankBranchName;
}
