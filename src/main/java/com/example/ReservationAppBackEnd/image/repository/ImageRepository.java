package com.example.ReservationAppBackEnd.image.repository;

import com.example.ReservationAppBackEnd.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
