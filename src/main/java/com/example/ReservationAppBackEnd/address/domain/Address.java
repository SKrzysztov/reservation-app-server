package com.example.ReservationAppBackEnd.address.domain;

import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private int buildingNumber;
    private String city;
    private String zipCode;
    private String country;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "service_provider_id")
    private CustomServiceProvider serviceProvider;
}
