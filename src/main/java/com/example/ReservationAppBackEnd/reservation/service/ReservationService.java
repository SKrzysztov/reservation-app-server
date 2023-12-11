package com.example.ReservationAppBackEnd.reservation.service;



import com.example.ReservationAppBackEnd.customService.domein.CustomService;
import com.example.ReservationAppBackEnd.customService.service.CustomServiceService;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.service.CustomServiceProviderService;
import com.example.ReservationAppBackEnd.reservation.api.ReservationRequest;
import com.example.ReservationAppBackEnd.reservation.domain.Reservation;
import com.example.ReservationAppBackEnd.reservation.repository.ReservationRepository;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.error.UnauthorizedException;
import com.example.ReservationAppBackEnd.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {

    @Autowired
    private final ReservationRepository reservationRepository;
    private final CustomServiceProviderService customServiceProviderService;
    private final UserService userService;
    private final CustomServiceService customServiceService;

    @Transactional
    public Reservation createReservation(ReservationRequest reservationRequest) {
        if(!userService.isUserLoggedIn()){
            throw new UnauthorizedException("you must login");
        }
        User user = userService.getLoggedUser();
        CustomServiceProvider serviceProvider = customServiceProviderService.getExistingServiceProvider(reservationRequest.serviceProviderId());
        CustomService service = customServiceService.getServiceById(reservationRequest.serviceId());
        Reservation reservation = Reservation.builder()
                .user(user)
                .service(service)
                .serviceProvider(serviceProvider)
                .startTime(reservationRequest.startTime())
                .endTime(calculateEndTimeOfReservation(reservationRequest.startTime(),service.getDuration()))
                .build();
            return reservationRepository.save(reservation);
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
    private LocalDateTime calculateEndTimeOfReservation(LocalDateTime startTime, Duration duration) {
        if (startTime == null || duration == null) {
            throw new IllegalArgumentException("Invalid input: startTime or duration cannot be null");
        }
        return startTime.plus(duration);
    }


}
