package com.example.ReservationAppBackEnd.customServiceProvider.repository;

import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.StatusCustomServiceProvider;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomServiceProviderRepository extends JpaRepository<CustomServiceProvider, Long> {
    List<CustomServiceProvider> findAllByStatusCustomServiceProvider(StatusCustomServiceProvider status);
    List<CustomServiceProvider> findByUserId(Long userId);

    @Query("SELECT csp FROM CustomServiceProvider csp " +
            "LEFT JOIN csp.customServiceCategory cat " +
            "WHERE (:categoryNames IS NULL OR cat.name IN :categoryNames) " +
            "AND csp.statusCustomServiceProvider = :status")
    List<CustomServiceProvider> findAllFilteredByCategoryNamesAndStatus(
            @Param("categoryNames") List<String> categoryNames,
            @Param("status") StatusCustomServiceProvider status);


}
