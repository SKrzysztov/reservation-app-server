package com.example.ReservationAppBackEnd.customService.service;

import com.example.ReservationAppBackEnd.customService.api.CustomServiceRequest;
import com.example.ReservationAppBackEnd.customService.domein.CustomService;
import com.example.ReservationAppBackEnd.customService.domein.CustomServiceStatus;
import com.example.ReservationAppBackEnd.customService.repository.CustomServiceRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.repository.CustomServiceProviderRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.service.CustomServiceProviderService;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.error.UnauthorizedException;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomServiceService {

    private final CustomServiceRepository serviceRepository;
    private final CustomServiceProviderRepository serviceProviderRepository;
    private final UserService userService;
    private final CustomServiceProviderService customServiceProviderService;

    public CustomService createService(@Valid CustomServiceRequest customServiceRequest) {
        CustomServiceProvider serviceProvider = customServiceProviderService.getServiceProvider(customServiceRequest.serviceProviderId());
        if (!userService.getLoggedUser().equals(serviceProvider.getUser())) {
            throw new UnauthorizedException("You are not authorized to create a service for this provider.");
        }
        CustomService createService = CustomService.builder()
                .name(customServiceRequest.name())
                .description(customServiceRequest.description())
                .price(customServiceRequest.price())
                .status(CustomServiceStatus.AVAILABLE)  // Dostosuj status według potrzeb
                .serviceProvider(serviceProvider)
                .duration(Duration.ofMinutes(customServiceRequest.durationInMinutes()))
                .build();
        return serviceRepository.save(createService);
    }

    public void deleteService(Long id) {
        Optional<CustomService> existingService = serviceRepository.findById(id);

        if (existingService.isPresent()) {
            CustomService serviceToDelete = existingService.get();

            CustomServiceProvider serviceProvider = serviceToDelete.getServiceProvider();
            User user =userService.getLoggedUser();
            if (!user.equals(serviceProvider.getUser())) {
                throw new UnauthorizedException("You are not authorized to delete this service.");
            }

            serviceRepository.delete(serviceToDelete);
        } else {
            throw new NotFoundException("Record doesn't exist.");
        }
    }

    public CustomService getServiceById(Long id) {
        Optional<CustomService> service = serviceRepository.findById(id);

        if (service.isPresent()) {
            return service.get();
        } else {
            throw new NotFoundException("Record doesn't exist.");
        }
    }

    public CustomService updateService(Long id, CustomService updatedService) {
        Optional<CustomService> existingService = serviceRepository.findById(id);

        if (existingService.isPresent()) {
            CustomService serviceToUpdate = existingService.get();
            User user = userService.getLoggedUser();
            CustomServiceProvider serviceProvider = serviceToUpdate.getServiceProvider();
            if (!user.equals(serviceProvider.getUser())) {
                throw new UnauthorizedException("You are not authorized to update this service.");
            }

            if (updatedService.getName() != null) {
                serviceToUpdate.setName(updatedService.getName());
            }

            if (updatedService.getDescription() != null) {
                serviceToUpdate.setDescription(updatedService.getDescription());
            }

            if (updatedService.getStatus() != null) {
                serviceToUpdate.setStatus(updatedService.getStatus());
            }

            return serviceRepository.save(serviceToUpdate);
        } else {
            throw new NotFoundException("Record doesn't exist.");
        }
    }

    public List<CustomService> getAllServices() {
        return serviceRepository.findAll();
    }

    public CustomService setServiceStatusAvailable(Long serviceId) {
        Optional<CustomService> existingService = serviceRepository.findById(serviceId);

        if (existingService.isPresent()) {
            CustomService serviceToUpdate = existingService.get();

            serviceToUpdate.setStatus(CustomServiceStatus.AVAILABLE);

            return serviceRepository.save(serviceToUpdate);
        }

        throw new NotFoundException("Record doesn't exist.");
    }


}
