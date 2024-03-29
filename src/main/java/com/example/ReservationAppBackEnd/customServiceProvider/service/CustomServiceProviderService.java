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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<CustomServiceProvider> getServiceProviderByUser(Long userId) {
        User user = userService.getLoggedUser();
        if (!userId.equals(user.getId())) {
            throw new UnauthorizedException("You are not logged in");
        } else {
            return customServiceProviderRepository.findByUserId(userId);
        }
    }
    public List<CustomServiceProvider> getAllCustomServiceProvider(){
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
                .startTime(serviceProviderRequest.startTime())
                .endTime(serviceProviderRequest.endTime())
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

            return customServiceProviderRepository.findAllByStatusCustomServiceProvider(StatusCustomServiceProvider.WAITING);
        } else {
            throw new UnauthorizedException("You are not authorize to search waiting serviceproviders");
        }
    }

    public List<CustomServiceProvider> getAllAvailableCustomServiceProviders() {
        return customServiceProviderRepository.findAllByStatusCustomServiceProvider(StatusCustomServiceProvider.AVAILABLE);
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
        LocalTime newStartTime = serviceProviderRequest.startTime();
        LocalTime newEndTime = serviceProviderRequest.endTime();
        AddressRequest addressRequest = serviceProviderRequest.address();

        if (newName != null && !newName.equals(serviceProvider.getName())) {
            serviceProvider.setName(newName);
        }
        if (newStartTime != null && !newStartTime.equals(serviceProvider.getStartTime())) {
            serviceProvider.setStartTime(newStartTime);
        }
        if (newEndTime != null && !newEndTime.equals(serviceProvider.getEndTime())) {
            serviceProvider.setEndTime(newEndTime);
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

    public CustomServiceProvider setCustomServiceProviderAvailable(Long id) {
        if(!userService.isUserLoggedIn()){
            throw new UnauthorizedException("You are not authorize to create Category");
        }
        User user = userService.getLoggedUser();
        CustomServiceProvider serviceProvider = getServiceProvider(id);
        if(serviceProvider.getStatusCustomServiceProvider().equals(StatusCustomServiceProvider.WAITING))
        {
            if(user.getRole().equals(Role.ADMIN)) {
            serviceProvider.setStatusCustomServiceProvider(StatusCustomServiceProvider.AVAILABLE);
            return customServiceProviderRepository.save(serviceProvider);
        }
        else {
            throw new UnauthorizedException("You are not authorize to create Category");
        }
        } else if(serviceProvider.getStatusCustomServiceProvider().equals(StatusCustomServiceProvider.UNAVAILABLE)){
            if(user.getId().equals(serviceProvider.getUser().getId())){
                serviceProvider.setStatusCustomServiceProvider(StatusCustomServiceProvider.AVAILABLE);
                return customServiceProviderRepository.save(serviceProvider);
            }
            else {
                throw new UnauthorizedException("You are not authorize to create Category");
            }
        }
        else {
            throw new UnauthorizedException("You are not authorize to create Category");
        }
    }
    public CustomServiceProvider setCustomServiceProviderUnavailable(Long id) {
        if (!userService.isUserLoggedIn()) {
            throw new UnauthorizedException("You are not authorize to create Category");
        }
        User user = userService.getLoggedUser();
        CustomServiceProvider serviceProvider = getServiceProvider(id);
        if (user.getId().equals(serviceProvider.getUser().getId())) {
            if(!serviceProvider.getStatusCustomServiceProvider().equals(StatusCustomServiceProvider.WAITING)){
                serviceProvider.setStatusCustomServiceProvider(StatusCustomServiceProvider.UNAVAILABLE);
                return customServiceProviderRepository.save(serviceProvider);
            }
            else {
                throw new UnauthorizedException("You are not authorize to create Category");
            }
        } else {
            throw new UnauthorizedException("You are not authorize to create Category");
        }
    }
    public CustomServiceProvider setImageServiceProvider(Long serviceProviderId, ImageRequest imageRequest) {
        CustomServiceProvider serviceProvider = customServiceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new NotFoundException("Service Provider not found"));

        User loggedInUser = userService.getLoggedUser();
        if (!loggedInUser.equals(serviceProvider.getUser())) {
            throw new UnauthorizedException("You are not allowed to update this Service Provider");
        }

        Image image = imageService.saveImage(imageRequest);
        serviceProvider.setImage(image);
        return customServiceProviderRepository.save(serviceProvider);
    }
    public List<CustomServiceProvider> getAllFilteredByCategoryNames(List<String> categoryNames) {
        return customServiceProviderRepository.findAllFilteredByCategoryNamesAndStatus(categoryNames, StatusCustomServiceProvider.AVAILABLE);
    }
}
