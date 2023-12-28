package com.example.demo.model.mapper;

import com.example.demo.entity.Apartment;
import com.example.demo.entity.Customer;
import com.example.demo.model.DTO.ApartmentDTO;
import com.example.demo.model.DTO.CustomerDTO;

public class EntityToDto {
    public static CustomerDTO CustomerToDto(Customer customer) {
        return CustomerDTO.builder()
                .age(String.valueOf(customer.getAge()))
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .address(customer.getAddress())
                .build();
    }

    public static ApartmentDTO ApartmentToDto(Apartment apartment) {
        return ApartmentDTO.builder()
                .address(apartment.getAddress())
                .retailPrice(String.valueOf(apartment.getRetailPrice()))
                .numberOfRoom(String.valueOf(apartment.getNumberOfRoom()))
                .build();
    }
}
