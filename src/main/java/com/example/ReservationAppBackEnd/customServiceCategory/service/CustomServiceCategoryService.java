package com.example.ReservationAppBackEnd.customServiceCategory.service;

import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import com.example.ReservationAppBackEnd.customServiceCategory.repository.CustomServiceCategoryRepository;
import com.example.ReservationAppBackEnd.customServiceCategory.api.CustomServiceCategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomServiceCategoryService {

    private final CustomServiceCategoryRepository customServiceCategoryRepository;

    @Autowired
    public CustomServiceCategoryService(CustomServiceCategoryRepository customServiceCategoryRepository) {
        this.customServiceCategoryRepository = customServiceCategoryRepository;
    }

    public List<CustomServiceCategory> getAllCategories() {
        return customServiceCategoryRepository.findAll();
    }

    public Optional<CustomServiceCategory> getCategoryById(Long id) {
        return customServiceCategoryRepository.findById(id);
    }

    public CustomServiceCategory createCategory(CustomServiceCategoryRequest categoryRequest) {
        CustomServiceCategory category = CustomServiceCategory.builder()
                .name(categoryRequest.name())
                .build();

        return customServiceCategoryRepository.save(category);
    }

    public CustomServiceCategory updateCategory(Long id, CustomServiceCategoryRequest categoryRequest) {
        Optional<CustomServiceCategory> optionalCategory = customServiceCategoryRepository.findById(id);

        if (optionalCategory.isPresent()) {
            CustomServiceCategory category = optionalCategory.get();
            category.setName(categoryRequest.name());

            return customServiceCategoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found with id: " + id);
        }
    }

    public void deleteCategory(Long id) {
        customServiceCategoryRepository.deleteById(id);
    }

    // Dodaj inne metody związane z operacjami CRUD na kategoriach usług, jeśli są potrzebne
}
