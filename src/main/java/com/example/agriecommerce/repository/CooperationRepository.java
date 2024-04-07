package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Cooperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CooperationRepository extends JpaRepository<Cooperation, Long> {
    Optional<List<Cooperation>> findBySupplierId(Long supplierId);
    Optional<List<Cooperation>> findByUserId(Long userId);
    @Query("SELECT SUM(c.requireYield) FROM Cooperation c " +
            "WHERE c.field.id = :fieldId AND c.supplier.id = :supplierId")
    Double calculateYieldAccepted(Long fieldId, Long supplierId);
}
