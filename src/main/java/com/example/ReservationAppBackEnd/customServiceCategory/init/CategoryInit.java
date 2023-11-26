package com.example.ReservationAppBackEnd.customServiceCategory.init;


import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import com.example.ReservationAppBackEnd.customServiceCategory.repository.CustomServiceCategoryRepository;
import com.example.ReservationAppBackEnd.user.domain.Role;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryInit implements CommandLineRunner {

    CustomServiceCategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if(categoryRepository.count() == 0) {
            CustomServiceCategory category1 = CustomServiceCategory.builder()
                    .id(null)
                    .name("Category1")
                    .build();
            CustomServiceCategory category2 = CustomServiceCategory.builder()
                    .id(null)
                    .name("Category2")
                    .build();
            CustomServiceCategory category3 = CustomServiceCategory.builder()
                    .id(null)
                    .name("Category3")
                    .build();

            categoryRepository.save(category1);
            categoryRepository.save(category2);
            categoryRepository.save(category3);
        }
    }
}

