package com.example.ReservationAppBackEnd.customServiceProvider.repository;

import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomServiceProviderRepository extends JpaRepository<CustomServiceProvider, Long> {
    List<CustomServiceProvider> findByCustomServiceCategoryName(String categoryName);
}
