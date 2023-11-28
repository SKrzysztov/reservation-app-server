package com.example.ReservationAppBackEnd.customServiceCategory.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomServiceCategoryRequest(
        @NotBlank(message = "Name cannot be blank")
        @Size(min= 5,max = 255, message = "Name cannot exceed 255 characters")
        String name
) {
}
