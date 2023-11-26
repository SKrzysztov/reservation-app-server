package com.example.ReservationAppBackEnd.customServiceProvider.service;

import com.example.ReservationAppBackEnd.address.domain.Address;
import com.example.ReservationAppBackEnd.customServiceCategory.domain.CustomServiceCategory;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.StatusCustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.repository.CustomServiceProviderRepository;
import com.example.ReservationAppBackEnd.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomServiceProviderService {

    private final CustomServiceProviderRepository customServiceProviderRepository;

    @Autowired
    public CustomServiceProviderService(CustomServiceProviderRepository customServiceProviderRepository) {
        this.customServiceProviderRepository = customServiceProviderRepository;
    }

    public CustomServiceProvider createServiceProvider(String name, Address address, CustomServiceCategory category, User user) {
        CustomServiceProvider serviceProvider = CustomServiceProvider.builder()
                .name(name)
                .address(address)
                .customServiceCategory(category)
                .user(user)
                .statusCustomServiceProvider(StatusCustomServiceProvider.WAITING)
                .build();

        return customServiceProviderRepository.save(serviceProvider);
    }

    public List<CustomServiceProvider> getAllServiceProviders() {
        return customServiceProviderRepository.findAll();
    }

    public Optional<CustomServiceProvider> getServiceProviderById(Long id) {
        return customServiceProviderRepository.findById(id);
    }

    public CustomServiceProvider updateServiceProvider(Long id, String name, Address address, CustomServiceCategory category) {
        Optional<CustomServiceProvider> optionalServiceProvider = customServiceProviderRepository.findById(id);

        if (optionalServiceProvider.isPresent()) {
            CustomServiceProvider serviceProvider = optionalServiceProvider.get();
            serviceProvider.setName(name);
            serviceProvider.setAddress(address);
            serviceProvider.setCustomServiceCategory(category);

            return customServiceProviderRepository.save(serviceProvider);
        } else {
            // Możesz rzucić wyjątek lub zwrócić null, zależy od preferencji
            return null;
        }
    }

    public void deleteServiceProvider(Long id) {
        customServiceProviderRepository.deleteById(id);
    }
}
