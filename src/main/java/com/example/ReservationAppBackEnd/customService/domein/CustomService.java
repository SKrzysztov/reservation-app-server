package com.example.ReservationAppBackEnd.customService.domein;

import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.reservation.domain.Reservation;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.util.List;
@Entity
@Table(name = "customServices")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String name;

    @Enumerated
    private CustomServiceStatus status;

    private float price;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    private CustomServiceProvider serviceProvider;

    private Duration duration;

    @JsonProperty("durationInMinutes")
    public Long getDurationInMinutes() {
        return duration.toMinutes();
    }
}
