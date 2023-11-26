package com.example.ReservationAppBackEnd.customService.service;

import com.example.ReservationAppBackEnd.customService.api.CustomServiceRequest;
import com.example.ReservationAppBackEnd.customService.domein.CustomService;
import com.example.ReservationAppBackEnd.customService.domein.CustomServiceStatus;
import com.example.ReservationAppBackEnd.customService.repository.CustomServiceRepository;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import com.example.ReservationAppBackEnd.error.UnauthorizedException;
import com.example.ReservationAppBackEnd.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomServiceService {

    @Autowired
    private CustomServiceRepository serviceRepository;

    public CustomService createService(User user, CustomServiceRequest customServiceRequest) {
        CustomService createService = CustomService.builder()
                .name(customServiceRequest.name())
                .description(customServiceRequest.description())
                .build();
        return serviceRepository.save(createService);
    }

    public void deleteService(User user, Long id) {
        Optional<CustomService> existingService = serviceRepository.findById(id);

        if (existingService.isPresent()) {
            CustomService serviceToDelete = existingService.get();

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

        return null;
    }
}
