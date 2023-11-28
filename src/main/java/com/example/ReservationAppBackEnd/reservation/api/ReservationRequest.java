package com.example.ReservationAppBackEnd.reservation.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservationRequest(
        @NotNull(message = "Service provider cannot be null")
        @Valid
        CustomServiceProvider serviceProvider,
        @NotNull(message = "Start time cannot be null")
        LocalDateTime startTime,
        @NotNull(message = "End time cannot be null")
        LocalDateTime endTime
) {
}
