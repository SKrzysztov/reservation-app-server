package com.example.ReservationAppBackEnd.image.api;

import lombok.Builder;

@Builder
public record ImageRequest(
        byte[] data
) {
}
