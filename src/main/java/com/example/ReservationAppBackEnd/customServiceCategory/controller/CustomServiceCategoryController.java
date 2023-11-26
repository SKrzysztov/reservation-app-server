package com.example.ReservationAppBackEnd.customServiceCategory.controller;

import com.example.ReservationAppBackEnd.customServiceCategory.api.CustomServiceCategoryRequest;
import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import com.example.ReservationAppBackEnd.customServiceCategory.service.CustomServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customServiceCategory")
public class CustomServiceCategoryController {

    private final CustomServiceCategoryService customServiceCategoryService;

    @Autowired
    public CustomServiceCategoryController(CustomServiceCategoryService customServiceCategoryService) {
        this.customServiceCategoryService = customServiceCategoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomServiceCategory>> getAllCategories() {
        List<CustomServiceCategory> categories = customServiceCategoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomServiceCategory> getCategoryById(@PathVariable Long id) {
        Optional<CustomServiceCategory> category = customServiceCategoryService.getCategoryById(id);
        return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<CustomServiceCategory> createCategory(
            @RequestBody CustomServiceCategoryRequest categoryRequest) {
        CustomServiceCategory createdCategory = customServiceCategoryService.createCategory(categoryRequest);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomServiceCategory> updateCategory(
            @PathVariable Long id, @RequestBody CustomServiceCategoryRequest categoryRequest) {
        try {
            CustomServiceCategory updatedCategory = customServiceCategoryService.updateCategory(id, categoryRequest);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        customServiceCategoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Dodaj inne metody obsługujące inne żądania, jeśli są potrzebne
}
