package com.example.ReservationAppBackEnd.reservation.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReservationDetails {
    @Id
    private Long id;
    private Date dateFrom;
    private Date dateTo;
    private String description;
}
