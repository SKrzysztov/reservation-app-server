package com.example.ReservationAppBackEnd.customServiceProvider.api;

import com.example.ReservationAppBackEnd.address.api.AddressRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CustomServiceProviderRequest(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @Valid
        @NotNull(message = "Address cannot be null")
        AddressRequest address,
        @NotNull(message = "Category ID cannot be null")
        Long categoryId

) {
}
