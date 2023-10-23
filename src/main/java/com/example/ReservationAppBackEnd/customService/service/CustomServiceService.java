package com.example.ReservationAppBackEnd.customService.service;


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
    public CustomService save(User user) {
        CustomService customService = new CustomService();
        customService.setStatus(CustomServiceStatus.WAITING);
        customService.setUser(user);

        return serviceRepository.save(customService);
    }
}
