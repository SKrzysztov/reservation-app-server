package com.example.ReservationAppBackEnd.customService.models;

import com.example.ReservationAppBackEnd.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "services")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomService {
    @Id
    private long id;
    private String description;
    private String name;
    @Enumerated
    private CustomServiceStatus status;
    @ManyToOne(optional = false)
    private User user;
}
