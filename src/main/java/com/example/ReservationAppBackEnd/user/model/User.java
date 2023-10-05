package com.example.ReservationAppBackEnd.user.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated
    Role role;
}
