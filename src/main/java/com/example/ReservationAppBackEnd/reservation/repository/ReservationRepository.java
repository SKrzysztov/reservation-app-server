package com.example.ReservationAppBackEnd.reservation.repository;

import com.example.ReservationAppBackEnd.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
