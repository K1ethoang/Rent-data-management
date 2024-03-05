package com.example.demo.service.interfaces;

import com.example.demo.entity.Customer;
import com.example.demo.model.DTO.customer.CustomerDTO;
import com.example.demo.model.DTO.customer.CustomerUpdateDTO;
import com.example.demo.model.DTO.paging.APIPageableDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface CustomerService {
    Map<String, Object> getAll(Pageable pageable);

    Customer getCustomer(String id);

    CustomerDTO getCustomerDTO(String id);

    CustomerDTO create(CustomerDTO customerDTO);

    // Read file from upload
    List<Object> loadCustomers(MultipartFile[] files);

    Map<String, Object> loadCustomer(MultipartFile file);

    CustomerDTO update(String id, CustomerUpdateDTO customerUpdate);

    CustomerDTO delete(String id);

    File exportCsv(boolean getTemplate);

    Map<String, Object> search(String query,Pageable pageable);
}
