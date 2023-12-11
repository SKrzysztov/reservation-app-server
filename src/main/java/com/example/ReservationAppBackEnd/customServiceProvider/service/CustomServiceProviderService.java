package com.example.ReservationAppBackEnd.customServiceProvider.service;

import com.example.ReservationAppBackEnd.address.api.AddressRequest;
import com.example.ReservationAppBackEnd.address.domain.Address;
import com.example.ReservationAppBackEnd.address.service.AddressService;
import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import com.example.ReservationAppBackEnd.customServiceCategory.service.CustomServiceCategoryService;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.StatusCustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.repository.CustomServiceProviderRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.api.CustomServiceProviderRequest;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.error.UnauthorizedException;
import com.example.ReservationAppBackEnd.image.api.ImageRequest;
import com.example.ReservationAppBackEnd.image.domain.Image;
import com.example.ReservationAppBackEnd.image.service.ImageService;
import com.example.ReservationAppBackEnd.user.domain.Role;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.user.service.UserService;
import jakarta.validation.Valid;
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
    private final CustomServiceCategoryService customServiceCategoryService;
    private final ImageService imageService;
    public CustomServiceProvider saveServiceProvider(CustomServiceProvider serviceProvider) {
        return customServiceProviderRepository.save(serviceProvider);
    }
    public List<CustomServiceProvider> getAllServiceProviders() {
        return customServiceProviderRepository.findAll();
    }

    public CustomServiceProvider createServiceProvider(@Valid CustomServiceProviderRequest serviceProviderRequest, Long categoryId) {
        if (!userService.isUserLoggedIn()) {
            throw new UnauthorizedException("You are not logged in");
        } else {
        AddressRequest addressRequest = serviceProviderRequest.address();
        Address address = addressService.createAddress(addressRequest);
        User loggedInUser = userService.getLoggedUser();
        CustomServiceCategory category = customServiceCategoryService.getExistingCategory(categoryId);
        CustomServiceProvider serviceProvider = CustomServiceProvider.builder()
                .name(serviceProviderRequest.name())
                .address(address)
                .user(loggedInUser)
                .statusCustomServiceProvider(StatusCustomServiceProvider.WAITING)
                .customServiceCategory(category)
                .build();
        return customServiceProviderRepository.save(serviceProvider);
        }
    }


    public List<CustomServiceProvider> getAllWaitingCustomServiceProviders() {
        if (!userService.isUserLoggedIn()) {
            throw new UnauthorizedException("You are not authorize to create Category");
        }
        User user = userService.getLoggedUser();
        if (user.getRole().equals(Role.ADMIN)) {

            return customServiceProviderRepository.findAllByStatusCustomServiceProvider("WAITING");
        } else {
            throw new UnauthorizedException("You are not authorize to search waiting serviceproviders");
        }
    }

    public List<CustomServiceProvider> getAllAvailableCustomServiceProviders() {
        return customServiceProviderRepository.findAllByStatusCustomServiceProvider("AVAILABLE");
    }
    public CustomServiceProvider getServiceProvider(Long serviceProviderId) {
        Optional<CustomServiceProvider> serviceProvider = customServiceProviderRepository.findById(serviceProviderId);
        return serviceProvider.orElseThrow(() -> new NotFoundException("Service provider not found with id: " + serviceProviderId));
    }

    public void deleteServiceProvider(Long id) {
        CustomServiceProvider serviceProvider = customServiceProviderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service Provider not found"));

        User loggedInUser = userService.getLoggedUser();
        if (!loggedInUser.equals(serviceProvider.getUser())) {
            throw new UnauthorizedException("You are not allowed to delete this Service Provider");
        }
        customServiceProviderRepository.deleteById(id);
    }

    public CustomServiceProvider updateServiceProvider(Long id, @Valid CustomServiceProviderRequest serviceProviderRequest) {
        CustomServiceProvider serviceProvider = customServiceProviderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service Provider not found"));

        User loggedInUser = userService.getLoggedUser();
        if (!loggedInUser.equals(serviceProvider.getUser())) {
            throw new UnauthorizedException("You are not allowed to update this Service Provider");
        }

        String newName = serviceProviderRequest.name();
        AddressRequest addressRequest = serviceProviderRequest.address();

        if (newName != null && !newName.equals(serviceProvider.getName())) {
            serviceProvider.setName(newName);
        }

        if (addressRequest != null) {
            Address updatedAddress = addressService.updateAddress(serviceProvider.getAddress().getId(), addressRequest);
            serviceProvider.setAddress(updatedAddress);
        }

        Long newCategoryId = serviceProviderRequest.categoryId();
        if (newCategoryId != null) {
            Long currentCategoryId = serviceProvider.getCustomServiceCategory().getId();

            if (!newCategoryId.equals(currentCategoryId)) {
                serviceProvider.setCustomServiceCategory(customServiceCategoryService.getExistingCategory(newCategoryId));
            }
        }

        return customServiceProviderRepository.save(serviceProvider);
    }

    public void setCustomServiceProviderAvailable(Long id) {
        if(!userService.isUserLoggedIn()){
            throw new UnauthorizedException("You are not authorize to create Category");
        }
        User user = userService.getLoggedUser();
        if(user.getRole().equals(Role.ADMIN)) {
            CustomServiceProvider serviceProvider = getServiceProvider(id);
            serviceProvider.setStatusCustomServiceProvider(StatusCustomServiceProvider.AVAILABLE);
        }
        else {
            throw new UnauthorizedException("You are not authorize to create Category");
        }
    }
    public CustomServiceProvider addImageToServiceProvider(Long serviceProviderId, ImageRequest imageRequest) {
        CustomServiceProvider serviceProvider = customServiceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("CustomServiceProvider not found with id: " + serviceProviderId));

        Image savedImage = imageService.saveImage(imageRequest);
        serviceProvider.setImage(savedImage);

        return customServiceProviderRepository.save(serviceProvider);
    }
    public CustomServiceProvider updateServiceProviderImage(Long serviceProviderId, ImageRequest imageRequest) {
        CustomServiceProvider serviceProvider = customServiceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("CustomServiceProvider not found with id: " + serviceProviderId));

        Image newImage = imageService.saveImage(imageRequest);
        serviceProvider.setImage(newImage);

        return customServiceProviderRepository.save(serviceProvider);
    }

}
