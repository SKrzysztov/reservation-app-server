package com.example.ReservationAppBackEnd.user.controller;


import com.example.ReservationAppBackEnd.user.api.LoginRequest;
import com.example.ReservationAppBackEnd.user.api.LoginResponse;
import com.example.ReservationAppBackEnd.user.api.RegisterRequest;
import com.example.ReservationAppBackEnd.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = userService.authenticateAndGetToken(loginRequest.login(), loginRequest.password());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(new LoginResponse(token));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
    }
}
