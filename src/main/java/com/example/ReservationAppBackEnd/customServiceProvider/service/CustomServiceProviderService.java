package com.example.ReservationAppBackEnd.customServiceProvider.service;

import com.example.ReservationAppBackEnd.address.domain.Address;
import com.example.ReservationAppBackEnd.address.service.AddressService;
import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import com.example.ReservationAppBackEnd.customServiceCategory.repository.CustomServiceCategoryRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.StatusCustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.repository.CustomServiceProviderRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.api.CustomServiceProviderRequest;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomServiceProviderService {

    private final CustomServiceProviderRepository customServiceProviderRepository;
    private final AddressService addressService;
    private final UserService userService;
    private final CustomServiceCategoryRepository customServiceCategoryRepository; // Dodane pole repozytorium

    public CustomServiceProvider createServiceProvider(CustomServiceProviderRequest serviceProviderRequest, Long categoryId) {
        Address address = addressService.createAddress(serviceProviderRequest.address());

        User loggedInUser = userService.getLoggedUser();

        CustomServiceCategory category = getExistingCategory(categoryId);

        CustomServiceProvider serviceProvider = CustomServiceProvider.builder()
                .name(serviceProviderRequest.name())
                .address(address)
                .user(loggedInUser)
                .statusCustomServiceProvider(StatusCustomServiceProvider.WAITING)
                .customServiceCategory(category)
                .build();
        return customServiceProviderRepository.save(serviceProvider);
    }

    private CustomServiceCategory getExistingCategory(Long categoryId) {
        Optional<CustomServiceCategory> category = customServiceCategoryRepository.findById(categoryId);
        return category.orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
    }

    public List<CustomServiceProvider> getAllServiceProviders() {
        return customServiceProviderRepository.findAll();
    }

    public Optional<CustomServiceProvider> getServiceProviderById(Long id) {
        return customServiceProviderRepository.findById(id);
    }

    public CustomServiceProvider updateServiceProvider(Long id, CustomServiceProviderRequest serviceProviderRequest) {
        Optional<CustomServiceProvider> optionalServiceProvider = customServiceProviderRepository.findById(id);

        if (optionalServiceProvider.isPresent()) {
            CustomServiceProvider serviceProvider = optionalServiceProvider.get();

            if (userService.getLoggedUser() == serviceProvider.getUser()) {
                // Tworzenie nowego obiektu dostawcy usług z istniejącymi danymi
                CustomServiceProvider updatedServiceProvider = CustomServiceProvider.builder()
                        .id(serviceProvider.getId())
                        .name(serviceProviderRequest.name())
                        .address(serviceProvider.getAddress())  // Tutaj adres pozostaje taki sam
                        .user(serviceProvider.getUser())
                        .statusCustomServiceProvider(serviceProvider.getStatusCustomServiceProvider())
                        .customServiceCategory(serviceProvider.getCustomServiceCategory())
                        .build();

                // Aktualizacja danych dostawcy usług
                return customServiceProviderRepository.save(updatedServiceProvider);
            } else {
                throw new RuntimeException("You have no access to do it");
            }
        } else {
            throw new RuntimeException("ServiceProvider not found with id: " + id);
        }
    }


    public void deleteServiceProvider(Long id) {
        Optional<CustomServiceProvider> optionalServiceProvider = customServiceProviderRepository.findById(id);
        if (optionalServiceProvider.isPresent()) {
            CustomServiceProvider serviceProvider = optionalServiceProvider.get();
            if(userService.getLoggedUser() == serviceProvider.getUser()) {
                customServiceProviderRepository.deleteById(id);
            }
        }
        else {
            throw new RuntimeException("ServiceProvider not found with id: " + id);
        }
    }
}
