package com.example.ReservationAppBackEnd.testAuthentication;

import com.example.ReservationAppBackEnd.user.api.LoginRequest;
import com.example.ReservationAppBackEnd.user.api.LoginResponse;
import com.example.ReservationAppBackEnd.user.api.RegisterRequest;
import com.example.ReservationAppBackEnd.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;



@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class DemoController {
    @GetMapping("/json")
    public MyData getJsonData() {
        // Create a MyData instance and set the 'xd' field
        MyData jsonData = new MyData("Hello, world!");
        return jsonData;
    }
    @GetMapping("/jsonadmin")
    public MyData getJsonDataAdmin() {
        // Create a MyData instance and set the 'xd' field
        MyData jsonData = new MyData("Hello, admin");
        return jsonData;
    }
}
