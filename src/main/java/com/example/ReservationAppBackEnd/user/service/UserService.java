package com.example.ReservationAppBackEnd.user.service;

import com.example.ReservationAppBackEnd.security.ApplicationUserDetails;
import com.example.ReservationAppBackEnd.security.TokenProvider;
import com.example.ReservationAppBackEnd.user.api.RegisterRequest;
import com.example.ReservationAppBackEnd.user.model.Role;
import com.example.ReservationAppBackEnd.user.model.User;
import com.example.ReservationAppBackEnd.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserValidator userValidator;


    public User getLoggedUser() {
        String login = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal.getClass().isAssignableFrom(ApplicationUserDetails.class))
                .map(principal -> (ApplicationUserDetails) principal)
                .map(ApplicationUserDetails::getLogin)
                .orElseThrow(() -> new IllegalStateException("User cannot be extracted"));
        return findUserByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public Optional<User> findUserByLogin(String login) {
        return userRepository.findByLoginIgnoreCase(login);
    }

    public void register(RegisterRequest registerRequest) {
        Optional<User> t = Optional.empty();

        userValidator.validate(registerRequest);

        User registerUser = User.builder()
                .login(registerRequest.login())
                .password(passwordEncoder.encode(registerRequest.password()))
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .email(registerRequest.email())
                .role(Role.USER)
                .build();

        userRepository.save(registerUser);
    }

    public String authenticateAndGetToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.generate(authentication);
    }
}