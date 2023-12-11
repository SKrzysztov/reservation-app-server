package com.example.ReservationAppBackEnd.image.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ImageRequest {

    private byte[] data;

    public ImageRequest() {
    }

    public ImageRequest(byte[] data) {
        this.data = data;
    }


}

