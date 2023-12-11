package com.example.ReservationAppBackEnd.reservation.domain;

import com.example.ReservationAppBackEnd.customService.domein.CustomService;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Date;




@Entity
@Table(name = "reservations")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    private CustomServiceProvider serviceProvider;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private CustomService service;

    @JsonFormat(pattern = "yy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yy-MM-dd HH:mm")
    private LocalDateTime endTime;

}
