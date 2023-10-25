package com.example.ReservationAppBackEnd.reservation.controller;

import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reservation/")
public class ReservationController {

    @PostMapping()
    public void createReservation(){

    }
}
