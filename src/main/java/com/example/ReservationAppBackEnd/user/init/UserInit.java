package com.example.ReservationAppBackEnd.user.init;

import com.example.ReservationAppBackEnd.user.model.Role;
import com.example.ReservationAppBackEnd.user.model.User;
import com.example.ReservationAppBackEnd.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserInit implements CommandLineRunner {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0) {
            User user = User.builder()
                    .id(null)
                    .email("useruser@wp.pl")
                    .login("user123")
                    .firstName("User")
                    .password(passwordEncoder.encode("User123"))
                    .role(Role.USER)
                    .build();

            User admin = User.builder()
                    .id(null)
                    .email("adminadmin@wp.pl")
                    .login("admin123")
                    .firstName("Admin")
                    .password(passwordEncoder.encode("Admin123"))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(user);
            userRepository.save(admin);
        }
    }
}
