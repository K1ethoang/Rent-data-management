package com.example.demo.model.mapper;

import com.example.demo.entity.Apartment;
import com.example.demo.entity.Contract;
import com.example.demo.entity.Customer;
import com.example.demo.entity.User;
import com.example.demo.model.DTO.apartment.ApartmentDTO;
import com.example.demo.model.DTO.contract.ContractDTO;
import com.example.demo.model.DTO.customer.CustomerDTO;
import com.example.demo.model.DTO.user.UserDto;

public class EntityToDto {
    public static CustomerDTO customerToDto(Customer customer) {
        return CustomerDTO.builder()
                .age(String.valueOf(customer.getAge()))
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .address(customer.getAddress())
                .build();
    }

    public static ApartmentDTO apartmentToDto(Apartment apartment) {
        return ApartmentDTO.builder()
                .address(apartment.getAddress())
                .retailPrice(apartment.getRetailPrice())
                .numberOfRoom(String.valueOf(apartment.getNumberOfRoom()))
                .build();
    }

    public static ContractDTO contractToDto(Contract contract) {
        return ContractDTO.builder()
                .startDate(contract.getStartDate().toString())
                .endDate(contract.getEndDate().toString())
                .apartmentId(contract.getApartment().getId())
                .customerId(contract.getCustomer().getId())
                .build();
    }

    public static UserDto userToDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .avatar(user.getAvatar())
                .createDate(user.getCreateDate())
                .state(user.getState())
                .build();
    }
}
