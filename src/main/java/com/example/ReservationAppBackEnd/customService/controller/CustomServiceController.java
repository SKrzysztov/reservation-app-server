package com.example.ReservationAppBackEnd.customService.controller;

import com.example.ReservationAppBackEnd.customService.api.CustomServiceRequest;
import com.example.ReservationAppBackEnd.customService.domein.CustomService;
import com.example.ReservationAppBackEnd.customService.service.CustomServiceService;
import com.example.ReservationAppBackEnd.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/custom-services")
@RequiredArgsConstructor
public class CustomServiceController {

    private final CustomServiceService customServiceService;

    @PostMapping("/create")
    public ResponseEntity<CustomService> createService(
            @RequestBody @Valid CustomServiceRequest customServiceRequest) {
        CustomService createdService = customServiceService.createService(customServiceRequest);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomService> getServiceById(@PathVariable Long id) {
        CustomService service = customServiceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomService>> getAllServices() {
        List<CustomService> services = customServiceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomService> updateService(
            @PathVariable Long id,
            @RequestBody CustomService updatedService,
            @RequestAttribute("user") User user) {
        CustomService updated = customServiceService.updateService(user, id, updatedService);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteService(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {
        customServiceService.deleteService(user, id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/set-available/{id}")
    public ResponseEntity<CustomService> setServiceStatusAvailable(@PathVariable Long id) {
        CustomService updated = customServiceService.setServiceStatusAvailable(id);
        return ResponseEntity.ok(updated);
    }
}
