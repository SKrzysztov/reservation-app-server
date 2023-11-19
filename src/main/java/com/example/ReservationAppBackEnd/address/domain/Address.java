package com.example.ReservationAppBackEnd.address.domain;

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
    private String city;
    private String zipCode;
    private String country;
}
