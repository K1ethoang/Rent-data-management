package com.example.demo.service.implement;

import com.example.demo.entity.Customer;
import com.example.demo.exception.AppException;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.interfaces.CustomerInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService implements CustomerInterface {
    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getOneById(String id) throws AppException {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isPresent()) return optionalCustomer.get();
        else
            throw new AppException(404, "Customer not found");
    }

    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(String id, Map<String, Object> payload) throws AppException {

        Customer customerFromDB = customerRepository.findById(id).orElse(null);

        if (customerFromDB != null) {

            payload.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Customer.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, customerFromDB, value);
                }
            });
            return customerRepository.save(customerFromDB);
        } else
            throw new AppException(404, "Customer not found");
    }

    @Override
    public void delete(String id) throws AppException {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isPresent()) {
            Customer cus = optionalCustomer.get();

            customerRepository.delete(cus);
        } else
            throw new AppException(404, "Customer not found");
    }
}
