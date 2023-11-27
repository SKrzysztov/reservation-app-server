package com.example.ReservationAppBackEnd.address.api;

import lombok.Builder;

@Builder
public record AddressRequest(

        String street,
        int buildingNumber,
        String city,
        String zipCode,
        String country
) {
}
