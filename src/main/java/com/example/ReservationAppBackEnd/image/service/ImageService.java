package com.example.ReservationAppBackEnd.image.service;

import com.example.ReservationAppBackEnd.image.api.ImageRequest;
import com.example.ReservationAppBackEnd.image.domain.Image;
import com.example.ReservationAppBackEnd.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    public Image saveImage(ImageRequest imageRequest) {
        // Use the builder to create an instance of Image
        Image image = Image.builder()
                .data(imageRequest.data())
                .build();

        return imageRepository.save(image);
    }
}
