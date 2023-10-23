package com.example.ReservationAppBackEnd.customService.repository;

import com.example.ReservationAppBackEnd.customService.models.CustomService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomServiceRepository extends JpaRepository<CustomService,Long> {

}
