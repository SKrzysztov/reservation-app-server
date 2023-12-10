package com.example.ReservationAppBackEnd.address.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder

public record AddressRequest(
        @NotBlank(message = "Street cannot be blank")
        String street,
        @Positive(message = "Building number must be a positive number")
        int buildingNumber,
        @NotBlank(message = "City cannot be blank")
        String city,
        @Pattern(regexp = "\\d{2}-\\d{3}", message = "Zip code must have the format XX-XXX")
        String zipCode,
        @NotEmpty(message = "Country cannot be empty")
        String country
) {
}
