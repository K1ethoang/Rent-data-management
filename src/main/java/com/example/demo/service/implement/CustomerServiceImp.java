package com.example.demo.service.implement;

import com.example.demo.entity.Customer;
import com.example.demo.exception.AppException;
import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.message.CustomerMessage;
import com.example.demo.model.DTO.CustomerDTO;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.interfaces.CustomerService;
import com.example.demo.utils.MyUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Log4j2
public class CustomerServiceImp implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;

    @Override
    public List<CustomerDTO> getAll() throws NoContentException {
        List<Customer> customerList = customerRepository.findAll();

        if (customerList.isEmpty()) throw new NoContentException(CustomerMessage.EMPTY_LIST);

        return mapper.map(customerList, new TypeToken<List<CustomerDTO>>() {
        }.getType());
    }

    @Override
    public CustomerDTO getOneById(String id) throws NotFoundException {
        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer != null) return customerEntityToDTO(customer);
        else
            throw new NotFoundException(CustomerMessage.NOT_FOUND);
    }

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) throws AppException {
        return customerEntityToDTO(customerRepository.save(validateObject(customerDTOtoEntity(customerDTO))));
    }

    @Override
    public CustomerDTO update(String id, Map<String, Object> payload) throws NotFoundException {
        Customer customerFromDB = customerRepository.findById(id).orElse(null);

        if (customerFromDB != null) {

            payload.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Customer.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, customerFromDB, value);
                }
            });


            return customerEntityToDTO(customerRepository.save(validateObject(customerFromDB)));
        } else
            throw new NotFoundException(CustomerMessage.NOT_FOUND);
    }

    @Override
    public CustomerDTO delete(String id) throws NotFoundException {
        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer != null) {
            customerRepository.delete(customer);
            return customerEntityToDTO(customer);
        } else
            throw new NotFoundException(CustomerMessage.NOT_FOUND);
    }

    private Customer validateObject(Customer customer) {
        int age = customer.getAge();
        if (age <= 0 || MyUtils.isNumeric(String.valueOf(age))) {
            throw new AppException(HttpStatus.BAD_REQUEST, CustomerMessage.INVALID_DATA);
        }

        if (customer.getStatus().isEmpty())
            customer.setStatus("Normal");

        return customer;
    }

    private Customer customerDTOtoEntity(CustomerDTO customerDTO) {
        return mapper.map(customerDTO, Customer.class);
    }

    private CustomerDTO customerEntityToDTO(Customer customer) {
        return mapper.map(customer, CustomerDTO.class);
    }
}
