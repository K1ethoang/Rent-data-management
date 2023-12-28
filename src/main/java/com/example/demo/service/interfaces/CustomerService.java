package com.example.demo.service.interfaces;

import com.example.demo.model.DTO.CustomerDTO;
import com.example.demo.model.DTO.CustomerUpdateDTO;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getAll();

    CustomerDTO getCustomerDTO(String id);

    CustomerDTO create(CustomerDTO customerDTO);

    CustomerDTO update(String id, CustomerUpdateDTO customerUpdate);

    CustomerDTO delete(String id);

//    Customer customerDTOToEntity(CustomerDTO customerDTO);
//
//    CustomerDTO customerEntityToDTO(Customer customer);
}
