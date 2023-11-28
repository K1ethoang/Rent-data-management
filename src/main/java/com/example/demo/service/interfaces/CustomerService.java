package com.example.demo.service.interfaces;

import com.example.demo.model.DTO.CustomerDTO;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    List<CustomerDTO> getAll();

    CustomerDTO getOneById(String id);

    CustomerDTO create(CustomerDTO customerDTO);

    CustomerDTO update(String id, Map<String, Object> payload);

    CustomerDTO delete(String id);
}
