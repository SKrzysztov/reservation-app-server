package com.example.ReservationAppBackEnd.customServiceProvider.controller;

import com.example.ReservationAppBackEnd.customServiceProvider.api.CustomServiceProviderRequest;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.service.CustomServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customServiceProvider")
public class CustomServiceProviderController {

    private final CustomServiceProviderService customServiceProviderService;

    @Autowired
    public CustomServiceProviderController(CustomServiceProviderService customServiceProviderService) {
        this.customServiceProviderService = customServiceProviderService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomServiceProvider>> getAllServiceProviders() {
        List<CustomServiceProvider> serviceProviders = customServiceProviderService.getAllServiceProviders();
        return new ResponseEntity<>(serviceProviders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomServiceProvider> getServiceProviderById(@PathVariable Long id) {
        Optional<CustomServiceProvider> serviceProvider = customServiceProviderService.getServiceProviderById(id);
        return serviceProvider.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<CustomServiceProvider> createServiceProvider(
            @RequestBody CustomServiceProviderRequest serviceProviderRequest) {
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteServiceProvider(@PathVariable Long id) {
        customServiceProviderService.deleteServiceProvider(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Dodaj inne metody obsługujące inne żądania, jeśli są potrzebne
}
