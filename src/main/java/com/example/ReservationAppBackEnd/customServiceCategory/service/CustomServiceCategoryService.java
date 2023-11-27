package com.example.ReservationAppBackEnd.customServiceCategory.service;

import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import com.example.ReservationAppBackEnd.customServiceCategory.repository.CustomServiceCategoryRepository;
import com.example.ReservationAppBackEnd.customServiceCategory.api.CustomServiceCategoryRequest;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.user.domain.Role;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomServiceCategoryService {

    private final CustomServiceCategoryRepository customServiceCategoryRepository;
    private final UserService userService;


    public List<CustomServiceCategory> getAllCategories() {
        return customServiceCategoryRepository.findAll();
    }

    public Optional<CustomServiceCategory> getCategoryById(Long id) {
        return customServiceCategoryRepository.findById(id);
    }
    public CustomServiceCategory getExistingCategoryById(Long categoryId) {
        return customServiceCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + categoryId));
    }
    public CustomServiceCategory createCategory(CustomServiceCategoryRequest categoryRequest) {
        User user = userService.getLoggedUser();
        if(user.getRole().equals(Role.ADMIN)) {
            CustomServiceCategory category = CustomServiceCategory.builder()
                    .name(categoryRequest.name())
                    .build();
            return customServiceCategoryRepository.save(category);
        }
        else {
            throw new RuntimeException("xd");
        }
    }

    public CustomServiceCategory updateCategory(Long id, CustomServiceCategoryRequest categoryRequest) {
        Optional<CustomServiceCategory> optionalCategory = customServiceCategoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            User user = userService.getLoggedUser();
            if(user.getRole().equals("ADMIN")) {
            CustomServiceCategory category = optionalCategory.get();
            category.setName(categoryRequest.name());

            return customServiceCategoryRepository.save(category);
        } else {
                throw new RuntimeException("Category not found with id: " + id);
            }
        }
            else {
            throw new RuntimeException("Category not found with id: " + id);
        }
    }

    public void deleteCategory(Long id) {
        Optional<CustomServiceCategory> optionalCategory = customServiceCategoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            User user = userService.getLoggedUser();
            if (user.getRole().equals("ADMIN")) {
                customServiceCategoryRepository.deleteById(id);
            } else {
                throw new RuntimeException("Category not found with id: " + id);
            }
        } else {
            throw new RuntimeException("Category not found with id: " + id);

        }

    }
            // Dodaj inne metody związane z operacjami CRUD na kategoriach usług, jeśli są potrzebne
}
