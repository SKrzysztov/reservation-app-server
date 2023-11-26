package com.example.ReservationAppBackEnd.customServiceProvider.domain;


import com.example.ReservationAppBackEnd.address.domain.Address;
import com.example.ReservationAppBackEnd.comment.domain.Comment;
import com.example.ReservationAppBackEnd.customService.domein.CustomService;
import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import com.example.ReservationAppBackEnd.reservation.domain.Reservation;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "customServiceProvider")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomServiceProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    private List<CustomService> servicesOffered;

    @OneToOne
    @JoinColumn(name = "category_id")
    private CustomServiceCategory customServiceCategory;

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @ManyToOne(optional = false)
    private User user;

    @Enumerated
    private StatusCustomServiceProvider statusCustomServiceProvider;

}
