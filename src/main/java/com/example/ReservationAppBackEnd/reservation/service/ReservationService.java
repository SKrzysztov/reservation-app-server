package com.example.ReservationAppBackEnd.reservation.service;



import com.example.ReservationAppBackEnd.customService.domein.CustomService;
import com.example.ReservationAppBackEnd.customService.service.CustomServiceService;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.repository.CustomServiceProviderRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.service.CustomServiceProviderService;
import com.example.ReservationAppBackEnd.reservation.api.ReservationRequest;
import com.example.ReservationAppBackEnd.reservation.domain.Reservation;
import com.example.ReservationAppBackEnd.reservation.repository.ReservationRepository;
import com.example.ReservationAppBackEnd.reservation.tools.AvailableTimeRange;
import com.example.ReservationAppBackEnd.reservation.tools.TimeRange;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.error.UnauthorizedException;
import com.example.ReservationAppBackEnd.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationService {

    @Autowired
    private final ReservationRepository reservationRepository;
    private final CustomServiceProviderService customServiceProviderService;
    private final UserService userService;
    private final CustomServiceService customServiceService;
    private final CustomServiceProviderRepository customServiceProviderRepository;
    @Transactional
    public Reservation createReservation(ReservationRequest reservationRequest) {
        if (!userService.isUserLoggedIn()) {
            throw new UnauthorizedException("You must login");
        }

        User user = userService.getLoggedUser();
        CustomServiceProvider serviceProvider = customServiceProviderService.getServiceProvider(reservationRequest.serviceProviderId());
        CustomService service = customServiceService.getServiceById(reservationRequest.serviceId());

        // Sprawdzenie, czy rezerwacja mieści się w dostępnym czasie dostawcy usług
        checkServiceAvailability(serviceProvider, reservationRequest.startTime(), calculateEndTimeOfReservation(reservationRequest.startTime(), service.getDuration()));

        // Sprawdzenie dostępności czasu przed utworzeniem rezerwacji
        checkAvailability(serviceProvider, reservationRequest.startTime(), calculateEndTimeOfReservation(reservationRequest.startTime(), service.getDuration()));

        Reservation reservation = Reservation.builder()
                .user(user)
                .service(service)
                .serviceProvider(serviceProvider)
                .startTime(reservationRequest.startTime())
                .endTime(calculateEndTimeOfReservation(reservationRequest.startTime(), service.getDuration()))
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
    public List<AvailableTimeRange> getAvailableTimeRanges(Long serviceProviderId, LocalDate date) {
        CustomServiceProvider serviceProvider = customServiceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new EntityNotFoundException("CustomServiceProvider not found"));

        List<Reservation> reservationsForDay = serviceProvider.getReservations().stream()
                .filter(reservation -> reservation.getStartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());

        List<TimeRange> unavailableTimeRanges = new ArrayList<>();
        for (Reservation reservation : reservationsForDay) {
            LocalDateTime startReservationTime = reservation.getStartTime();
            LocalDateTime endReservationTime = reservation.getEndTime();

            TimeRange timeRange = new TimeRange(startReservationTime, endReservationTime);
            unavailableTimeRanges.add(timeRange);
        }

        List<AvailableTimeRange> availableTimeRanges = new ArrayList<>();

        LocalDateTime workingStartTime = LocalDateTime.of(date, serviceProvider.getStartTime());
        LocalDateTime workingEndTime = LocalDateTime.of(date, serviceProvider.getEndTime());

        LocalDateTime currentTime = workingStartTime;
        for (TimeRange unavailableRange : unavailableTimeRanges) {
            if (currentTime.isBefore(unavailableRange.getStartTime())) {
                availableTimeRanges.add(new AvailableTimeRange(currentTime, unavailableRange.getStartTime()));
            }
            currentTime = unavailableRange.getEndTime();
        }

        if (currentTime.isBefore(workingEndTime)) {
            availableTimeRanges.add(new AvailableTimeRange(currentTime, workingEndTime));
        }

        return availableTimeRanges;
    }
    private void checkAvailability(CustomServiceProvider serviceProvider, LocalDateTime startTime, LocalDateTime endTime) {
        List<Reservation> existingReservations = serviceProvider.getReservations().stream()
                .filter(reservation ->
                        (startTime.isAfter(reservation.getStartTime()) && startTime.isBefore(reservation.getEndTime())) ||
                                (endTime.isAfter(reservation.getStartTime()) && endTime.isBefore(reservation.getEndTime())) ||
                                (startTime.isBefore(reservation.getStartTime()) && endTime.isAfter(reservation.getEndTime())) ||
                                (startTime.isEqual(reservation.getStartTime()) || endTime.isEqual(reservation.getEndTime())))
                .collect(Collectors.toList());

        if (!existingReservations.isEmpty()) {
            throw new RuntimeException("Chosen time slot is not available");
        }
    }
    private void checkServiceAvailability(CustomServiceProvider serviceProvider, LocalDateTime startTime, LocalDateTime endTime) {
        LocalTime serviceStartTime = serviceProvider.getStartTime();
        LocalTime serviceEndTime = serviceProvider.getEndTime();

        if (startTime.toLocalTime().isBefore(serviceStartTime) || endTime.toLocalTime().isAfter(serviceEndTime)
                && (!startTime.toLocalTime().equals(serviceStartTime) || !endTime.toLocalTime().equals(serviceEndTime))) {
            throw new RuntimeException("Chosen time slot is not within the service provider's working hours");
        }
    }

}
