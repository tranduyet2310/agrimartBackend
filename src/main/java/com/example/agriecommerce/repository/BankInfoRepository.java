package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.SupplierBankInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankInfoRepository extends JpaRepository<SupplierBankInfo, Long> {

}
