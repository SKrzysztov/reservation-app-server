package com.example.ReservationAppBackEnd.service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Table(name = "services")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Service {
    @Id
    private long id;
    private String description;
    private String name;
    @Enumerated
    private ServiceStatus status;
}
