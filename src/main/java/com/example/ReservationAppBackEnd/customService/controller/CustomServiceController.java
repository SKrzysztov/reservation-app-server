package com.example.ReservationAppBackEnd.customService.controller;

import com.example.ReservationAppBackEnd.customService.api.CustomServiceRequest;
import com.example.ReservationAppBackEnd.customService.domein.CustomService;
import com.example.ReservationAppBackEnd.customService.service.CustomServiceService;
import com.example.ReservationAppBackEnd.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            @RequestBody CustomService updatedService) {
        CustomService updated = customServiceService.updateService(id, updatedService);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteService(
            @PathVariable Long id) {
        customServiceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/set-available/{id}")
    public ResponseEntity<CustomService> setServiceStatusAvailable(@PathVariable Long id) {
        CustomService updated = customServiceService.setServiceStatusAvailable(id);
        return ResponseEntity.ok(updated);
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
}
