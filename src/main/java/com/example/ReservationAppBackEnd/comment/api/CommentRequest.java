package com.example.ReservationAppBackEnd.comment.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
        @NotBlank(message = "Content cannot be blank") String content,
        @Min(value = 1, message = "Opinion must be at least 1")
        @Max(value = 5, message = "Opinion cannot be greater than 5")
        int opinion,
        Long serviceProviderId

) {}
