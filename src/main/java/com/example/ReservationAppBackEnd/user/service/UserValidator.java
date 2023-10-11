package com.example.ReservationAppBackEnd.user.service;

import com.example.ReservationAppBackEnd.error.ApplicationError;
import com.example.ReservationAppBackEnd.error.ApplicationException;
import com.example.ReservationAppBackEnd.security.TokenProvider;
import com.example.ReservationAppBackEnd.user.api.RegisterRequest;
import com.example.ReservationAppBackEnd.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserValidator {
    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    void validate(RegisterRequest request) {
        List<ApplicationError> errors = new ArrayList<>();

        if (userRepository.findByLoginIgnoreCase(request.login()).isPresent()) {
            errors.add(ApplicationError.LOGIN_EXISTS);
        }
        if (userRepository.findByEmailIgnoreCase(request.email()).isPresent()) {
            errors.add(ApplicationError.EMAIL_EXISTS);
        }
        if (!errors.isEmpty()) {
            throw new ApplicationException(errors);
        }
    }
}
