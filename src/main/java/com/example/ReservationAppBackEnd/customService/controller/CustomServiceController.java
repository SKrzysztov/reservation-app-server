package com.example.ReservationAppBackEnd.customService.controller;

import com.example.ReservationAppBackEnd.customService.api.CustomServiceRequest;
import com.example.ReservationAppBackEnd.customService.models.CustomService;
import com.example.ReservationAppBackEnd.customService.service.CustomServiceService;
import com.example.ReservationAppBackEnd.user.model.User;
import com.example.ReservationAppBackEnd.user.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
