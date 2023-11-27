package com.example.ReservationAppBackEnd.reservation.controller;

import com.example.ReservationAppBackEnd.reservation.api.ReservationRequest;
import com.example.ReservationAppBackEnd.reservation.domain.Reservation;
import com.example.ReservationAppBackEnd.reservation.service.ReservationService;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;
    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation(
            @RequestBody @Valid ReservationRequest reservationRequest) {
        User user = userService.getLoggedUser();
        Reservation createdReservation = reservationService.createReservation(user, reservationRequest);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {
        reservationService.deleteReservation(user, id);
        return ResponseEntity.noContent().build();
    }
}
