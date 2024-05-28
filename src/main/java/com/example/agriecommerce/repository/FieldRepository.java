package com.example.agriecommerce.repository;

import com.example.agriecommerce.entity.Field;
import com.example.agriecommerce.utils.CropsNameStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, Long> {
    Optional<List<Field>> findBySupplierId(Long supplierId);
    @Query(nativeQuery = true, value = "SELECT * FROM tbl_field WHERE is_complete = :isComplete AND supplier_id= :fieldId")
    Optional<List<Field>> findByIdAndIsComplete(@Param("fieldId") Long fieldId, @Param("isComplete") boolean isComplete);
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM tbl_field f WHERE YEAR(f.date_created) = :year")
    long countTotalField(@Param("year") int year);
    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM tbl_field f WHERE MONTH(f.date_created) = :month AND YEAR(f.date_created) = :year")
    long countFieldByMonthAndYear(@Param("month") int month, @Param("year") int year);
    @Query(nativeQuery = true, value = "SELECT crops_name as cropsName, COUNT(*) as sum FROM tbl_field f " +
            "WHERE YEAR(f.date_created) = :year " +
            "AND f.is_complete = 0 " +
            "GROUP BY crops_name")
    List<CropsNameStatistic> getCropsNameStatistic(@Param("year") int year);
}
