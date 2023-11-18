package com.example.ReservationAppBackEnd.customServiceCategory.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "categories")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomServiceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
