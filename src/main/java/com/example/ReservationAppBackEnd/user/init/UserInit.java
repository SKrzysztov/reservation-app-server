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
            User admin1 = User.builder()
                    .id(null)
                    .email("admin1@wp.pl")
                    .login("administrator1")
                    .firstName("Admin")
                    .lastName("Admin")
                    .password(passwordEncoder.encode("Administrator"))
                    .role(Role.ADMIN)
                    .accountNonLocked(true)
                    .build();

            User admin2 = User.builder()
                    .id(null)
                    .email("admin2@wp.pl")
                    .login("Administrator2")
                    .firstName("Admin")
                    .lastName("Admin")
                    .password(passwordEncoder.encode("Administrator"))
                    .role(Role.ADMIN)
                    .accountNonLocked(false)
                    .build();

            userRepository.save(admin1);
            userRepository.save(admin2);
        }
    }
}
