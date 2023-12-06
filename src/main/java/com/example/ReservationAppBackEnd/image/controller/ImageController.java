package com.example.ReservationAppBackEnd.image.controller;

import com.example.ReservationAppBackEnd.image.api.ImageRequest;
import com.example.ReservationAppBackEnd.image.domain.Image;
import com.example.ReservationAppBackEnd.image.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<Image> uploadImage(@RequestBody ImageRequest imageRequest) {
        Image savedImage = imageService.saveImage(imageRequest);
        return new ResponseEntity<>(savedImage, HttpStatus.CREATED);
    }
}