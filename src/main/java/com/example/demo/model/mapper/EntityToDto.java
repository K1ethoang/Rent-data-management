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
                .id(customer.getId())
                .fullName(customer.getFullName())
                .citizenId(customer.getCitizenId())
                .address(customer.getAddress())
                .dob(customer.getDob().toString())
                .phoneNumber(customer.getPhoneNumber())
                .build();
    }

    public static ApartmentDTO apartmentToDto(Apartment apartment) {
        return ApartmentDTO.builder()
                .id(apartment.getId())
                .address(apartment.getAddress())
                .retailPrice(String.valueOf(apartment.getRetailPrice()))
                .numberOfRoom(String.valueOf(apartment.getNumberOfRoom()))
                .build();
    }

    public static ContractDTO contractToDto(Contract contract) {
        return ContractDTO.builder()
                .id(contract.getId())
                .customerId(contract.getId())
                .customerId(contract.getCustomer().getId())
                .apartmentId(contract.getApartment().getId())
                .startDate(contract.getStartDate().toString())
                .endDate(contract.getEndDate().toString())
                .createDate(contract.getCreateDate().toString())
                .retailPrice(contract.getRetailPrice().toString())
                .total(contract.getTotal().toString())
                .userId(contract.getUser().getId())
                .build();
    }

    public static UserDto userToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .createDate(String.valueOf(user.getCreateDate()))
                .fullName(user.getFullName())
                .active(user.getActive())
                .role(user.getRole().getName().name())
                .build();
    }
}
