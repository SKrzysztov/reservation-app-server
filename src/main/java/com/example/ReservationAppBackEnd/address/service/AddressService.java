package com.example.ReservationAppBackEnd.address.service;

import com.example.ReservationAppBackEnd.address.api.AddressRequest;
import com.example.ReservationAppBackEnd.address.domain.Address;
import com.example.ReservationAppBackEnd.address.repository.AddressRepository;
import com.example.ReservationAppBackEnd.customServiceProvider.domain.CustomServiceProvider;
import com.example.ReservationAppBackEnd.customServiceProvider.repository.CustomServiceProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomServiceProviderRepository customServiceProviderRepository;
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    public Address createAddress(AddressRequest addressRequest, Long serviceProviderId) {
        // Sprawdź istnienie dostawcy usług, jeśli jest podane id
        CustomServiceProvider serviceProvider = null;
        if (serviceProviderId != null) {
            serviceProvider = customServiceProviderRepository.findById(serviceProviderId)
                    .orElseThrow(() -> new RuntimeException("Service provider not found with id: " + serviceProviderId));
        }

        // Tworzenie adresu
        Address address = Address.builder()
                .street(addressRequest.street())
                .buildingNumber(addressRequest.buildingNumber())
                .city(addressRequest.city())
                .zipCode(addressRequest.zipCode())
                .country(addressRequest.country())
                .build();

        // Przypisanie adresu do dostawcy usług, jeśli dostawca jest dostępny
        if (serviceProvider != null) {
            serviceProvider.setAddress(address);
        }

        // Zapisanie adresu
        return addressRepository.save(address);
    }

    public Address updateAddress(Long id, AddressRequest addressRequest) {
        Optional<Address> optionalAddress = addressRepository.findById(id);

        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            address.setStreet(addressRequest.street());
            address.setBuildingNumber(addressRequest.buildingNumber());
            address.setCity(addressRequest.city());
            address.setZipCode(addressRequest.zipCode());
            address.setCountry(addressRequest.country());

            return addressRepository.save(address);
        } else {
            throw new RuntimeException("Address not found with id: " + id);
        }
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}
