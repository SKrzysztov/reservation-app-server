package com.example.ReservationAppBackEnd.customServiceProvider.domain;


import com.example.ReservationAppBackEnd.address.domain.Address;
import com.example.ReservationAppBackEnd.comment.domain.Comment;
import com.example.ReservationAppBackEnd.customService.domein.CustomService;
import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import com.example.ReservationAppBackEnd.image.domain.Image;
import com.example.ReservationAppBackEnd.reservation.domain.Reservation;
import com.example.ReservationAppBackEnd.user.domain.User;
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



    @OneToOne
    @JoinColumn(name = "category_id")
    private CustomServiceCategory customServiceCategory;

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL)
    private List<CustomService> customServices;

    @ManyToOne(optional = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @Enumerated
    private StatusCustomServiceProvider statusCustomServiceProvider;

    private double averageOpinion;
    public void recalculateAverageOpinion() {
        if (comments != null && !comments.isEmpty()) {
            double averageRating = comments.stream()
                    .mapToDouble(Comment::getOpinion)
                    .average()
                    .orElse(0.0);
            setAverageOpinion(averageRating);
        } else {
            setAverageOpinion(0.0);
        }
    }
}
