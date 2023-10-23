package com.example.ReservationAppBackEnd.customService.controller;

import com.example.ReservationAppBackEnd.customService.models.CustomService;
import com.example.ReservationAppBackEnd.customService.service.CustomServiceService;
import com.example.ReservationAppBackEnd.user.model.User;
import com.example.ReservationAppBackEnd.user.service.UserService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> createService(){
        User user = userService.getLoggedUser();
        CustomService newService =  customServiceService.save(user);
        return ResponseEntity.ok(newService);
    }
}
