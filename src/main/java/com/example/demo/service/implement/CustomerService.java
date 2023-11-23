package com.example.demo.service.implement;

import com.example.demo.entity.Customer;
import com.example.demo.exception.AppException;
import com.example.demo.message.CustomerMessage;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.interfaces.CustomerInterface;
import com.example.demo.utils.MyUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CustomerService implements CustomerInterface {
    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> getAll() throws AppException {
        List<Customer> customers = customerRepository.findAll();

        if (customers.isEmpty()) throw new AppException(HttpStatus.OK, CustomerMessage.EMPTY_LIST);

        return customers;
    }

    @Override
    public Customer getOneById(String id) throws AppException {
        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer != null) return customer;
        else
            throw new AppException(HttpStatus.NOT_FOUND, CustomerMessage.NOT_FOUND);
    }

    @Override
    public Customer create(Customer customer) throws AppException {
        return customerRepository.save(validateObject(customer));
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

            return customerRepository.save(validateObject(customerFromDB));
        } else
            throw new AppException(HttpStatus.NOT_FOUND, CustomerMessage.NOT_FOUND);
    }

    @Override
    public String delete(String id) throws AppException {
        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer != null) {
            customerRepository.delete(customer);
            return CustomerMessage.SUCCESS_DELETED;

        } else
            throw new AppException(HttpStatus.NOT_FOUND, CustomerMessage.NOT_FOUND);
    }

    private Customer validateObject(Customer customer) {
        int age = customer.getAge();
        if (age <= 0 || !MyUtils.isNumeric(String.valueOf(age))) {
            throw new AppException(HttpStatus.BAD_REQUEST, CustomerMessage.INVALID_DATA);
        }

        if (customer.getStatus().isEmpty())
            customer.setStatus("Normal");

        return customer;
    }
}
