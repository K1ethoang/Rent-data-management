package com.example.demo.service.implement;

import com.example.demo.entity.Customer;
import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.message.CustomerMessage;
import com.example.demo.model.DTO.CustomerDTO;
import com.example.demo.model.DTO.CustomerUpdateDTO;
import com.example.demo.model.mapper.EntityToDto;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.interfaces.CustomerService;
import com.example.demo.utils.validator.CustomerValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class CustomerServiceImp implements CustomerService {
    private final CustomerRepository customerRepository;
    //    private final ModelMapper mapper;
    private final String DEFAULT_STATUS = "Normal";

    @Override
    public List<CustomerDTO> getAll() throws NoContentException {
        List<Customer> customerList = customerRepository.findAll();

        if (customerList.isEmpty()) throw new NoContentException(CustomerMessage.EMPTY_LIST);

        List<CustomerDTO> customerDTOList = new ArrayList<>();

        for (Customer customer : customerList) {
            customerDTOList.add(EntityToDto.customerToDto(customer));
        }

        return customerDTOList;
    }

    @Override
    public Customer getCustomer(String id) throws NotFoundException {
        Optional<Customer> optional = customerRepository.findById(id);

        if (optional.isEmpty()) throw new NotFoundException(CustomerMessage.NOT_FOUND);

        return optional.get();
    }

    @Override
    public CustomerDTO getCustomerDTO(String id) {
        Customer customer = getCustomer(id);

        return EntityToDto.customerToDto(customer);
    }

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) {
        CustomerValidator.validatorCustomerDTO(customerDTO);

        Customer customer = Customer.builder()
                .firstName(customerDTO.getFirstName().trim())
                .lastName(customerDTO.getLastName().trim())
                .address(customerDTO.getAddress().trim())
                .status(DEFAULT_STATUS)
                .age(Integer.parseInt(customerDTO.getAge().trim()))
                .build();

        customerRepository.save(customer);

        return customerDTO;
    }

    @Override
    public CustomerDTO update(String id, CustomerUpdateDTO customerUpdateDTO) {
        CustomerValidator.validatorCustomerUpdateDTO(customerUpdateDTO);

        Customer customerFromDB = getCustomer(id);

        if (customerUpdateDTO.getFirstName() != null) {
            customerFromDB.setFirstName(customerUpdateDTO.getFirstName().trim());
        }
        if (customerUpdateDTO.getLastName() != null) {
            customerFromDB.setLastName(customerUpdateDTO.getLastName().trim());
        }
        if (customerUpdateDTO.getAddress() != null) {
            customerFromDB.setAddress(customerUpdateDTO.getAddress().trim());
        }
        if (customerUpdateDTO.getAge() != null) {
            customerFromDB.setAge(Integer.parseInt(customerUpdateDTO.getAge().trim()));
        }

        customerRepository.save(customerFromDB);

        return EntityToDto.customerToDto(customerFromDB);
    }

    @Override
    public CustomerDTO delete(String id) throws NotFoundException {
        Customer customerToDelete = getCustomer(id);

        customerRepository.delete(customerToDelete);

        return EntityToDto.customerToDto(customerToDelete);
    }

//    public Customer customerDTOToEntity(CustomerDTO customerDTO) {
//        return mapper.map(customerDTO, Customer.class);
//    }
//
//    @Override
//    public CustomerDTO customerEntityToDTO(Customer customer) {
//        return mapper.map(customer, CustomerDTO.class);
//    }
}
