package com.example.ReservationAppBackEnd.customServiceCategory.repository;

import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomServiceCategoryRepository extends JpaRepository<CustomServiceCategory, Long> {
}
