package com.example.ReservationAppBackEnd.customService.repository;

import com.example.ReservationAppBackEnd.customService.domein.CustomService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomServiceRepository extends JpaRepository<CustomService,Long> {

}
