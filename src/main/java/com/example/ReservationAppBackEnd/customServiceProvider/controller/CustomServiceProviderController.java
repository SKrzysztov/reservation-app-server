package com.example.ReservationAppBackEnd.customServiceProvider.controller;

import com.example.ReservationAppBackEnd.customServiceProvider.api.CustomServiceProviderRequest;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.service.CustomServiceProviderService;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.error.UnauthorizedException;
import com.example.ReservationAppBackEnd.image.api.ImageRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "${app.cors.url}")
@RequestMapping("/api/customServiceProvider")
public class CustomServiceProviderController {

    private final CustomServiceProviderService customServiceProviderService;

    @Autowired
    public CustomServiceProviderController(CustomServiceProviderService customServiceProviderService) {
        this.customServiceProviderService = customServiceProviderService;
    }


    @GetMapping("/{serviceProviderId}")
    public ResponseEntity<CustomServiceProvider> getServiceProviderById(@PathVariable Long serviceProviderId) {
        try {
            CustomServiceProvider serviceProvider = customServiceProviderService.getServiceProvider(serviceProviderId);
            return new ResponseEntity<>(serviceProvider, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/All")
    public List<CustomServiceProvider> getAllServiceProvider(){
        return customServiceProviderService.getAllCustomServiceProvider();
    }
    @GetMapping("/byUser/{userId}")
    public List<CustomServiceProvider> getServiceProviderByUser(@PathVariable Long userId) {
        return customServiceProviderService.getServiceProviderByUser(userId);
    }
    @PostMapping("/create")
    public ResponseEntity<CustomServiceProvider> createServiceProvider(
            @RequestBody @Valid CustomServiceProviderRequest serviceProviderRequest) {
        CustomServiceProvider createdServiceProvider = customServiceProviderService.createServiceProvider(serviceProviderRequest, serviceProviderRequest.categoryId());
        return new ResponseEntity<>(createdServiceProvider, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomServiceProvider> updateServiceProvider(
            @PathVariable Long id, @RequestBody CustomServiceProviderRequest serviceProviderRequest) {
        try {
            CustomServiceProvider updatedServiceProvider = customServiceProviderService.updateServiceProvider(id, serviceProviderRequest);
            return new ResponseEntity<>(updatedServiceProvider, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/waiting")
    public List<CustomServiceProvider> getAllWaitingCustomServiceProviders() {
        return customServiceProviderService.getAllWaitingCustomServiceProviders();
    }

    @GetMapping("/available")
    public List<CustomServiceProvider> getAllAvailableCustomServiceProviders() {
        return customServiceProviderService.getAllAvailableCustomServiceProviders();
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteServiceProvider(@PathVariable Long id) {
        customServiceProviderService.deleteServiceProvider(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/{serviceProviderId}/image")
    public ResponseEntity<CustomServiceProvider> setImageServiceProvider(
            @PathVariable Long serviceProviderId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            // Zapisz obraz i zaktualizuj CustomServiceProvider
            ImageRequest imageRequest = new ImageRequest();
            imageRequest.setData(file.getBytes());

            CustomServiceProvider updatedServiceProvider = customServiceProviderService.setImageServiceProvider(serviceProviderId, imageRequest);

            // Zwróć zaktualizowany CustomServiceProvider w odpowiedzi
            return new ResponseEntity<>(updatedServiceProvider, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PutMapping("/setAvailable")
    public ResponseEntity<CustomServiceProvider> setCustomServiceProviderAvailable(@RequestParam Long id) {
        try {
            CustomServiceProvider updatedServiceProvider = customServiceProviderService.setCustomServiceProviderAvailable(id);
            return new ResponseEntity<>(updatedServiceProvider, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    @GetMapping("/filtered")
    public ResponseEntity<List<CustomServiceProvider>> getAllFilteredByCategoryNames(@RequestParam(required = false) List<String> categoryNames) {
        List<CustomServiceProvider> providers = customServiceProviderService.getAllFilteredByCategoryNames(categoryNames);
        return ResponseEntity.ok(providers);
    }
}
