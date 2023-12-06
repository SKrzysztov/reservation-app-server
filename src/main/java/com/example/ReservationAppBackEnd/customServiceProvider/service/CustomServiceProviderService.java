package com.example.ReservationAppBackEnd.customServiceProvider.service;

import com.example.ReservationAppBackEnd.address.api.AddressRequest;
import com.example.ReservationAppBackEnd.address.domain.Address;
import com.example.ReservationAppBackEnd.address.service.AddressService;
import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import com.example.ReservationAppBackEnd.customServiceCategory.repository.CustomServiceCategoryRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.StatusCustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.filter.CustomServiceProviderFilter;
import com.example.ReservationAppBackEnd.customServiceProvider.repository.CustomServiceProviderRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.api.CustomServiceProviderRequest;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomServiceProviderService {

    private final CustomServiceProviderRepository customServiceProviderRepository;
    private final AddressService addressService;
    private final UserService userService;
    private final CustomServiceCategoryRepository customServiceCategoryRepository;


    public CustomServiceProvider createServiceProvider(CustomServiceProviderRequest serviceProviderRequest, Long categoryId) {
        AddressRequest addressRequest = serviceProviderRequest.address();
        Address address = addressService.createAddress(addressRequest);

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
                Address existingAddress = serviceProvider.getAddress();
                AddressRequest newAddressRequest = serviceProviderRequest.address();

                // Aktualizuj pola adresu, jeśli zostały podane w żądaniu
                if (newAddressRequest != null) {
                    existingAddress.setStreet(newAddressRequest.street() != null ? newAddressRequest.street() : existingAddress.getStreet());
                    existingAddress.setBuildingNumber(newAddressRequest.buildingNumber() != 0 ? newAddressRequest.buildingNumber() : existingAddress.getBuildingNumber());
                    existingAddress.setCity(newAddressRequest.city() != null ? newAddressRequest.city() : existingAddress.getCity());
                    existingAddress.setZipCode(newAddressRequest.zipCode() != null ? newAddressRequest.zipCode() : existingAddress.getZipCode());
                    existingAddress.setCountry(newAddressRequest.country() != null ? newAddressRequest.country() : existingAddress.getCountry());
                }

                CustomServiceProvider updatedServiceProvider = CustomServiceProvider.builder()
                        .id(serviceProvider.getId())
                        .name(serviceProviderRequest.name() != null ? serviceProviderRequest.name() : serviceProvider.getName())
                        .user(serviceProvider.getUser())
                        .statusCustomServiceProvider(serviceProvider.getStatusCustomServiceProvider())
                        .customServiceCategory(serviceProvider.getCustomServiceCategory())
                        .address(existingAddress)
                        .build();

                updatedServiceProvider = customServiceProviderRepository.save(updatedServiceProvider);

                return updatedServiceProvider;
            } else {
                throw new RuntimeException("You have no access to do it");
            }
        } else {
            throw new RuntimeException("ServiceProvider not found with id: " + id);
        }
    }

    public CustomServiceProvider saveServiceProvider(CustomServiceProvider serviceProvider) {
        return customServiceProviderRepository.save(serviceProvider);
    }
    public CustomServiceProvider getExistingServiceProvider(Long serviceProviderId) {
        Optional<CustomServiceProvider> serviceProvider = customServiceProviderRepository.findById(serviceProviderId);
        return serviceProvider.orElseThrow(() -> new NotFoundException("Service provider not found with id: " + serviceProviderId));
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
    public List<CustomServiceProvider> filter(CustomServiceProviderFilter filter) {
        if (filter.getCategoryName() != null && filter.getCity() != null) {
            return customServiceProviderRepository.findByCustomServiceCategoryNameAndAddressCity(filter.getCategoryName(), filter.getCity());
        } else if (filter.getCategoryName() != null) {
            return customServiceProviderRepository.findByCustomServiceCategoryName(filter.getCategoryName());
        } else if (filter.getCity() != null) {
            return customServiceProviderRepository.findByAddressCity(filter.getCity());
        } else {
            return customServiceProviderRepository.findAll();
        }
    }
    public void setAvailableCustomServiceProvider(Long customServiceProviderId){

    }
}
