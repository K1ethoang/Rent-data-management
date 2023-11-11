package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private CustomerRepository customerRepository;

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer getOneById(String _id) {
        return customerRepository.findById(_id).get();
    }

    public Customer create(Customer _customer) {
        return customerRepository.save(_customer);
    }

    public Customer update(String _id, Customer _customer) {
        Customer customer = getOneById(_id);
        customer = _customer;
        return customerRepository.save(customer);
    }

    public void delete(String _id) {
        customerRepository.deleteById(_id);
    }
}
