package com.example.ReservationAppBackEnd.customServiceProvider.api;

import com.example.ReservationAppBackEnd.address.api.AddressRequest;
import lombok.Builder;

@Builder
public record CustomServiceProviderRequest(
   String name,
   AddressRequest address
) {}
