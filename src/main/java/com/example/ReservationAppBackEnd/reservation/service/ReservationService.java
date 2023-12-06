package com.example.ReservationAppBackEnd.reservation.service;



import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.reservation.api.ReservationRequest;
import com.example.ReservationAppBackEnd.reservation.domain.Reservation;
import com.example.ReservationAppBackEnd.reservation.repository.ReservationRepository;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.error.UnauthorizedException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;


    @Transactional
    public Reservation createReservation(User user, ReservationRequest reservationRequest) {
        if (hasCollisions(reservationRequest)) {
            throw new RuntimeException("Collision with other reservation");
        }else {
            CustomServiceProvider serviceProvider = reservationRequest.serviceProvider();
            Reservation reservation = Reservation.builder()
                    .user(user)
                    .serviceProvider(serviceProvider)
                    .startTime(reservationRequest.startTime())
                    .endTime(reservationRequest.endTime())
                    .build();

            return reservationRepository.save(reservation);
        }
    }

    public void deleteReservation(User user, Long id) {
        Optional<Reservation> existingReservation = reservationRepository.findById(id);

        if (existingReservation.isPresent()) {
            Reservation reservationToDelete = existingReservation.get();

            if (!user.equals(reservationToDelete.getUser())) {
                throw new UnauthorizedException("You are not authorized to delete this reservation.");
            }

            reservationRepository.delete(reservationToDelete);
        } else {
            throw new NotFoundException("Reservation doesn't exist.");
        }
    }

    public Reservation getReservationById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);

        if (reservation.isPresent()) {
            return reservation.get();
        } else {
            throw new NotFoundException("Reservation doesn't exist.");
        }
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }


    private boolean hasCollisions(ReservationRequest reservationRequest) {
        LocalDateTime startTime = reservationRequest.startTime();
        LocalDateTime endTime = reservationRequest.endTime();

        List<Reservation> existingReservations = reservationRepository.findByServiceProviderAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                reservationRequest.serviceProvider(), startTime, endTime);
        return existingReservations.stream()
                .anyMatch(existing ->
                        startTime.isBefore(existing.getEndTime()) && endTime.isAfter(existing.getStartTime()));
    }
}
