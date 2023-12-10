package com.example.ReservationAppBackEnd.customServiceCategory.controller;

import com.example.ReservationAppBackEnd.customServiceCategory.api.CustomServiceCategoryRequest;
import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import com.example.ReservationAppBackEnd.customServiceCategory.service.CustomServiceCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/api/customServiceCategory")
@Validated
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

   // @GetMapping("/{id}")
//    public ResponseEntity<CustomServiceCategory> getCategoryById(@PathVariable Long id) {
//        Optional<CustomServiceCategory> category = customServiceCategoryService.getCategoryById(id);
//        return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }

    @PostMapping("/create")

    public ResponseEntity<CustomServiceCategory> createCategory(
             @RequestBody @Valid CustomServiceCategoryRequest categoryRequest) {
        CustomServiceCategory createdCategory = customServiceCategoryService.createCategory(categoryRequest);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomServiceCategory> updateCategory(
            @PathVariable Long id, @RequestBody @Valid CustomServiceCategoryRequest categoryRequest) {
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
