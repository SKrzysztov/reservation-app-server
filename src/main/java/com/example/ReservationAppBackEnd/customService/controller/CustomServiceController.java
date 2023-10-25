package com.example.ReservationAppBackEnd.customService.controller;

import com.example.ReservationAppBackEnd.customService.api.CustomServiceRequest;
import com.example.ReservationAppBackEnd.customService.models.CustomService;
import com.example.ReservationAppBackEnd.customService.service.CustomServiceService;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.user.model.User;
import com.example.ReservationAppBackEnd.user.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class CustomServiceController {


    private final CustomServiceService customServiceService;
    private final UserService userService;
    @Autowired
    public CustomServiceController(CustomServiceService customServiceService, UserService userService) {
        this.customServiceService = customServiceService;
        this.userService = userService;
    }
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomService> createService(@Valid @RequestBody CustomServiceRequest serviceRequest){
        User user = userService.getLoggedUser();
        CustomService newService =  customServiceService.createService(user,serviceRequest);
        return ResponseEntity.ok(newService);
    }
    @PutMapping("/{id}")
    public CustomService updateService(@PathVariable Long id, @RequestBody CustomService customService) {
        User user = userService.getLoggedUser();
        return customServiceService.updateService(user, id, customService);
    }
    @GetMapping("/{id}")
    public CustomService getServiceById(@PathVariable Long id) {
        return customServiceService.getServiceById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteService(@PathVariable Long id) {
        User user = userService.getLoggedUser();
        customServiceService.deleteService(user, id);
    }
    @GetMapping("/")
    public List<CustomService> getAllServices(){
        return customServiceService.getAllServices();
    }
    @PutMapping("/{serviceId}/set-available")
    public ResponseEntity<CustomService> setServiceStatusAvailable(@PathVariable Long serviceId) {
        CustomService updatedService = customServiceService.setServiceStatusAvailable(serviceId);

        if (updatedService != null) {
            return new ResponseEntity<>(updatedService, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Możesz również rzucić odpowiedni wyjątek
        }
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
