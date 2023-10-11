package com.example.ReservationAppBackEnd.reservation.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reservation {
    @Id
    private Long id;
    @Enumerated
    ReservationStatus status;

    private String name;
}
