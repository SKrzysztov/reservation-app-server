package com.example.ReservationAppBackEnd.reservation.api;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        Long serviceProviderId,
        @NotNull(message = "Start time cannot be null")
        @JsonFormat(pattern = "yy-MM-dd HH:mm")
        LocalDateTime startTime,
        Long serviceId
) {
}
