package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Cooperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CooperationRepository extends JpaRepository<Cooperation, Long> {
    Optional<List<Cooperation>> findBySupplierId(Long supplierId);
    Optional<Page<Cooperation>> findBySupplierId(Long supplierId, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT * FROM tbl_cooperation " +
            "WHERE supplier_id = :supplierId AND field_id = :fieldId")
    Optional<Page<Cooperation>> findBySupplierIdAndFieldId(@Param("supplierId") Long supplierId, @Param("fieldId") Long fieldId, Pageable pageable);
    Optional<List<Cooperation>> findByUserId(Long userId);
    @Query("SELECT SUM(c.requireYield) FROM Cooperation c " +
            "WHERE c.field.id = :fieldId AND c.supplier.id = :supplierId")
    Double calculateYieldAccepted(Long fieldId, Long supplierId);

    List<Cooperation> findByPaymentStatusIsNullAndDateCreatedBefore(LocalDateTime date);
    @Query(nativeQuery = true, value = "SELECT * FROM tbl_cooperation " +
            "WHERE field_id = :fieldId ORDER BY date_created DESC")
    List<Cooperation> findByFieldIdAndDateCreated(@Param("fieldId") Long fieldId);
}
