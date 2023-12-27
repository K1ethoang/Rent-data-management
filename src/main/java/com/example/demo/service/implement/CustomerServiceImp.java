package com.example.demo.service.implement;

import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.helpers.Helper;
import com.example.demo.message.CustomerMessage;
import com.example.demo.model.DTO.CustomerDTO;
import com.example.demo.model.entity.Customer;
import com.example.demo.model.validator.CustomerValidator;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.interfaces.CustomerService;
import com.example.demo.utils.MyUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
    private final String DEFAULT_STATUS = "Ok";


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
    public CustomerDTO create(CustomerDTO customerDTO) {
        Helper.setAllFieldNullToEmpty(customerDTO);

        if (MyUtils.isEmpty(customerDTO.getStatus())) customerDTO.setStatus(DEFAULT_STATUS);

        CustomerValidator.validator(customerDTO);
        return customerEntityToDTO(customerRepository.save((customerDTOToEntity(customerDTO))));
    }

    @Override
    public CustomerDTO update(String id, Map<String, Object> payload) throws NotFoundException {
        // Find
        Customer customerFromDB = customerRepository.findById(id).orElse(null);
        if (customerFromDB == null) throw new NotFoundException(CustomerMessage.NOT_FOUND);

        // Map payload -> DTO to validate
        CustomerDTO customerFromPayload = mapper.map(payload, CustomerDTO.class);
        // Validate
        CustomerValidator.validator(customerFromPayload);

        CustomerDTO customerToUpdate = customerEntityToDTO(customerFromDB);

        payload.forEach((key, value) -> {
            if (value != null) {
                Field field = ReflectionUtils.findField(CustomerDTO.class, key);

                if (field != null) {
                    field.setAccessible(true);
                    log.info(key + " : " + value);
                    ReflectionUtils.setField(field, customerToUpdate, ReflectionUtils.getField(field, customerFromPayload));
                }
            }
        });

        return customerEntityToDTO(customerRepository.save(customerDTOToEntity(customerToUpdate)));
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

    public Customer customerDTOToEntity(CustomerDTO customerDTO) {
        return mapper.map(customerDTO, Customer.class);
    }

    @Override
    public CustomerDTO customerEntityToDTO(Customer customer) {
        return mapper.map(customer, CustomerDTO.class);
    }
}
