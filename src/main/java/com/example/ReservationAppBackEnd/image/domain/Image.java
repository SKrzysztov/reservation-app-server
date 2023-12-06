package com.example.ReservationAppBackEnd.image.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte[] data;
}
