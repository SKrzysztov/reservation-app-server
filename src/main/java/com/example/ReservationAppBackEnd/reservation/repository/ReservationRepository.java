package com.example.ReservationAppBackEnd.reservation.repository;

import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

 List<Reservation> findByServiceProviderAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
         CustomServiceProvider serviceProvider, LocalDateTime endTime, LocalDateTime startTime);
}
