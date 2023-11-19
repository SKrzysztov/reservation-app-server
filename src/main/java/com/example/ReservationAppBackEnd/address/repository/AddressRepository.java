package com.example.ReservationAppBackEnd.address.repository;

import com.example.ReservationAppBackEnd.address.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}