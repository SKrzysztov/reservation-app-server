package com.example.ReservationAppBackEnd.address.service;

import com.example.ReservationAppBackEnd.address.api.AddressRequest;
import com.example.ReservationAppBackEnd.address.domain.Address;
import com.example.ReservationAppBackEnd.address.repository.AddressRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.repository.CustomServiceProviderRepository;
import com.example.ReservationAppBackEnd.error.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address not found"));
    }

    public Address createAddress(@Valid AddressRequest addressRequest) {
        Address address = Address.builder()
                .street(addressRequest.street())
                .buildingNumber(addressRequest.buildingNumber())
                .city(addressRequest.city())
                .zipCode(addressRequest.zipCode())
                .country(addressRequest.country())
                .build();
        return addressRepository.save(address);
    }

    public Address updateAddress(Long id, @Valid AddressRequest addressRequest) {
        if (!addressRepository.existsById(id)) {
            throw new NotFoundException("Address not found");
        } else {
            Address existingAddress = getAddressById(id);
            existingAddress.setStreet(addressRequest.street());
            existingAddress.setBuildingNumber(addressRequest.buildingNumber());
            existingAddress.setCity(addressRequest.city());
            existingAddress.setZipCode(addressRequest.zipCode());
            existingAddress.setCountry(addressRequest.country());
            return addressRepository.save(existingAddress);
        }
    }
    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new NotFoundException("Address not found");
        }
        addressRepository.deleteById(id);
    }
}
