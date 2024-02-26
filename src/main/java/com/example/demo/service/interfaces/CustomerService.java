package com.example.demo.service.interfaces;

import com.example.demo.entity.Customer;
import com.example.demo.model.DTO.CustomerDTO;
import com.example.demo.model.DTO.CustomerUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface CustomerService {
    List<CustomerDTO> getAll();

    Customer getCustomer(String id);

    CustomerDTO getCustomerDTO(String id);

    CustomerDTO create(CustomerDTO customerDTO);

    // Read file from upload
    List<Object> loadCustomers(MultipartFile[] files);

    Map<String, Object> loadCustomer(MultipartFile file);

    CustomerDTO update(String id, CustomerUpdateDTO customerUpdate);

    CustomerDTO delete(String id);

    File exportCsv(boolean getTemplate);
}
