package com.example.ReservationAppBackEnd.customService.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.Duration;

@Builder
public record CustomServiceRequest (

    @Pattern(message = "Service Name is invalid", regexp = "[A-Za-z0-9]{3,30}")
    @NotBlank
    String name,

    @Pattern(message = "Service Description is invalid", regexp = ".{3,100}")
    @NotBlank
    String description,

    Float price,
    Long serviceProviderId,
    Long durationInMinutes
){}
