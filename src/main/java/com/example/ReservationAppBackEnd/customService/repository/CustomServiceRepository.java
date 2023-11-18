package com.example.ReservationAppBackEnd.customService.repository;

import com.example.ReservationAppBackEnd.customService.domein.CustomService;
import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomServiceRepository extends JpaRepository<CustomService,Long> {
    List<CustomService> findByCategory(CustomServiceCategory category);
}
