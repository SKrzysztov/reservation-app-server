package com.example.ReservationAppBackEnd.user.service;

import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.error.UnauthorizedException;
import com.example.ReservationAppBackEnd.security.ApplicationUserDetails;
import com.example.ReservationAppBackEnd.security.TokenProvider;
import com.example.ReservationAppBackEnd.user.api.ChangePasswordRequest;
import com.example.ReservationAppBackEnd.user.api.RegisterRequest;
import com.example.ReservationAppBackEnd.user.domain.Role;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
    public boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

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
                .id(null)
                .login(registerRequest.login())
                .password(passwordEncoder.encode(registerRequest.password()))
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .email(registerRequest.email())
                .accountNonLocked(true)
                .role(Role.USER)
                .build();

        userRepository.save(registerUser);
    }

    public String authenticateAndGetToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return tokenProvider.generate(authentication);
    }
    public void changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        if(user.getId().equals(getLoggedUser().getId())){
            if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
                throw new UnauthorizedException("Current password is incorrect.");
            }

            // Ustaw nowe hasło
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            // Zapisz użytkownika
            userRepository.save(user);
        }
        else{
            throw new UnauthorizedException("nie masz autoryzacji do zmiany hasla");
        }

    }
    public User lockAccount(String login) {
        Optional<User> existingUser = findUserByLogin(login);

        if (existingUser.isPresent()) {
            User userToLock = existingUser.get();

            userToLock.setAccountNonLocked(false);

            return userRepository.save(userToLock);
        }

        return null;
    }

}