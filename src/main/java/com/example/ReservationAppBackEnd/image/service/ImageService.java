package com.example.ReservationAppBackEnd.image.service;

import com.example.ReservationAppBackEnd.image.api.ImageRequest;
import com.example.ReservationAppBackEnd.image.domain.Image;
import com.example.ReservationAppBackEnd.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    public Image saveImage(ImageRequest imageRequest) {
        Image image = Image.builder()
                .data(imageRequest.getData())
                .build();

        return imageRepository.save(image);
    }
    public Optional<Image> getImage(Long imageId) {
        return imageRepository.findById(imageId);
    }
}
