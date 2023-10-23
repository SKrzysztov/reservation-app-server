package com.example.ReservationAppBackEnd.customService.service;


import com.example.ReservationAppBackEnd.customService.api.CustomServiceRequest;
import com.example.ReservationAppBackEnd.customService.models.CustomService;
import com.example.ReservationAppBackEnd.customService.models.CustomServiceStatus;
import com.example.ReservationAppBackEnd.customService.repository.CustomServiceRepository;
import com.example.ReservationAppBackEnd.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomServiceService {

    @Autowired
    private CustomServiceRepository serviceRepository;
    public CustomService createService( User user,CustomServiceRequest customServiceRequest) {
        CustomService createService = CustomService.builder()
                .name(customServiceRequest.name())
                .description(customServiceRequest.description())
                .status(CustomServiceStatus.WAITING)
                .user(user)
                .build();


        return serviceRepository.save(createService);
    }
}
