package com.example.ReservationAppBackEnd.reservation.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
@Builder
public record ReservationRequest (

            @Pattern(message = "Reservatiom is invalid", regexp = "[A-Za-z0-9]{3,30}")
            @NotBlank
            String name

    ){}
