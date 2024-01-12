package com.example.ReservationAppBackEnd.reservation.controller;

import com.example.ReservationAppBackEnd.customService.service.CustomServiceService;
import com.example.ReservationAppBackEnd.reservation.api.ReservationRequest;
import com.example.ReservationAppBackEnd.reservation.domain.Reservation;
import com.example.ReservationAppBackEnd.reservation.service.ReservationService;
import com.example.ReservationAppBackEnd.reservation.tools.AvailableTimeRange;
import com.example.ReservationAppBackEnd.reservation.tools.TimeRange;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;
    private final CustomServiceService serviceService;

    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation(
            @RequestBody @Valid ReservationRequest reservationRequest) {
        Reservation createdReservation = reservationService.createReservation(reservationRequest);
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    @GetMapping("/available-hours")
    public ResponseEntity<List<AvailableTimeRange>> getAvailableHours(
            @RequestParam Long serviceProviderId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AvailableTimeRange> availableHours = reservationService.getAvailableTimeRanges(serviceProviderId, date);
        return ResponseEntity.ok(availableHours);
    }
}
