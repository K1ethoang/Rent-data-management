package com.example.demo.service.interfaces;

import com.example.demo.entity.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerInterface {
    List<Customer> getAll();

    Customer getOneById(String id);

    Customer create(Customer customer);

    Customer update(String id, Map<String, Object> payload);

    String delete(String id);
}
