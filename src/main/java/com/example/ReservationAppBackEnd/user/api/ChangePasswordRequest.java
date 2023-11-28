package com.example.ReservationAppBackEnd.user.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ChangePasswordRequest {
    @Pattern(message = "Login is invalid", regexp = "[A-Za-z0-9]{3,14}")
    @NotBlank
    private String currentPassword;
    @Pattern(message = "Login is invalid", regexp = "[A-Za-z0-9]{3,14}")
    @NotBlank
    private String newPassword;
}