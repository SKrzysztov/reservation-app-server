package com.example.ReservationAppBackEnd.customService.service;

import com.example.ReservationAppBackEnd.customService.api.CustomServiceRequest;
import com.example.ReservationAppBackEnd.customService.domein.CustomService;
import com.example.ReservationAppBackEnd.customService.domein.CustomServiceStatus;
import com.example.ReservationAppBackEnd.customService.repository.CustomServiceRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.repository.CustomServiceProviderRepository;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.error.UnauthorizedException;
import com.example.ReservationAppBackEnd.user.domain.User;
import com.example.ReservationAppBackEnd.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomServiceService {

    private final CustomServiceRepository serviceRepository;
    private final CustomServiceProviderRepository serviceProviderRepository;
    private final UserService userService;

    public CustomService createService(@Valid CustomServiceRequest customServiceRequest) {
        CustomServiceProvider serviceProvider = getExistingServiceProvider(customServiceRequest.serviceProviderId());
        if (!userService.getLoggedUser().equals(serviceProvider.getUser())) {
            throw new UnauthorizedException("You are not authorized to create a service for this provider.");
        }

        CustomService createService = CustomService.builder()
                .name(customServiceRequest.name())
                .description(customServiceRequest.description())
                .status(CustomServiceStatus.AVAILABLE)  // Dostosuj status według potrzeb
                .serviceProvider(serviceProvider)
                .build();
        return serviceRepository.save(createService);
    }

    public void deleteService(User user, Long id) {
        Optional<CustomService> existingService = serviceRepository.findById(id);

        if (existingService.isPresent()) {
            CustomService serviceToDelete = existingService.get();

            // Sprawdzenie, czy użytkownik jest właścicielem dostawcy usług
            CustomServiceProvider serviceProvider = serviceToDelete.getServiceProvider();
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

    public CustomService updateService(User user, Long id, CustomService updatedService) {
        Optional<CustomService> existingService = serviceRepository.findById(id);

        if (existingService.isPresent()) {
            CustomService serviceToUpdate = existingService.get();

            // Sprawdzenie, czy użytkownik jest właścicielem dostawcy usług
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

    private CustomServiceProvider getExistingServiceProvider(Long serviceProviderId) {
        Optional<CustomServiceProvider> serviceProvider = serviceProviderRepository.findById(serviceProviderId);
        return serviceProvider.orElseThrow(() -> new NotFoundException("Service provider not found with id: " + serviceProviderId));
    }
}
