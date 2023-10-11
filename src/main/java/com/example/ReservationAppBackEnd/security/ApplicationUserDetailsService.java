package com.example.ReservationAppBackEnd.security;

import com.example.ReservationAppBackEnd.user.model.User;
import com.example.ReservationAppBackEnd.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@RequiredArgsConstructor
@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByLoginIgnoreCase(username)
                .map(this::mapToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    }

    private ApplicationUserDetails mapToUserDetails(User user) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
        return ApplicationUserDetails.builder()
//                .id(user.id())
                .email(user.getEmail())
                .login(user.getLogin())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
