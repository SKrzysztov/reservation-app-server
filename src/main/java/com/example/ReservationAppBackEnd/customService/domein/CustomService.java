package com.example.ReservationAppBackEnd.customService.domein;

import com.example.ReservationAppBackEnd.user.domain.User;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private String name;
    @Enumerated
    private CustomServiceStatus status;
    @ManyToOne(optional = false)
    private User user;
}
