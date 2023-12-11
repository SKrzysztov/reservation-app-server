package com.example.ReservationAppBackEnd.image.controller;

import com.example.ReservationAppBackEnd.image.api.ImageRequest;
import com.example.ReservationAppBackEnd.image.domain.Image;
import com.example.ReservationAppBackEnd.image.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @PostMapping
    public ResponseEntity<Image> uploadImage(@RequestParam("file") MultipartFile file) {
        ImageRequest imageRequest = new ImageRequest();
        try {
            imageRequest.setData(file.getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Image savedImage = imageService.saveImage(imageRequest);
        return new ResponseEntity<>(savedImage, HttpStatus.CREATED);
    }
}