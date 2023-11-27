package com.example.ReservationAppBackEnd.reservation.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservationRequest(
        CustomServiceProvider serviceProvider,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
