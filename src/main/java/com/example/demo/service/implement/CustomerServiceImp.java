package com.example.demo.service.implement;

import com.example.demo.entity.Customer;
import com.example.demo.exception.DuplicatedException;
import com.example.demo.exception.InValidException;
import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.helpers.CsvHelper;
import com.example.demo.message.CustomerMessage;
import com.example.demo.message.FileMessage;
import com.example.demo.model.DTO.CustomerDTO;
import com.example.demo.model.DTO.CustomerUpdateDTO;
import com.example.demo.model.mapper.EntityToDto;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.interfaces.CustomerService;
import com.example.demo.utils.validator.CustomerValidator;
import com.example.demo.utils.validator.FileValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class CustomerServiceImp implements CustomerService {
    private final CustomerRepository customerRepository;
    private final String DEFAULT_STATUS = "Normal";

    public static void checkDuplicated(CustomerDTO customerToCheck, List<Customer> customerList) throws DuplicatedException {
        boolean isDuplicate = false;

        for (Customer customer : customerList) {
            CustomerDTO customerFromList = EntityToDto.customerToDto(customer);

            if (!customerFromList.getFirstName().equalsIgnoreCase(customerToCheck.getFirstName()))
                continue;
            if (!customerFromList.getLastName().equalsIgnoreCase(customerToCheck.getLastName()))
                continue;
            if (!customerFromList.getAddress().equalsIgnoreCase(customerToCheck.getAddress()))
                continue;
            if (!customerFromList.getAge().equals(customerToCheck.getAge())) continue;

            isDuplicate = true;
            break;
        }

        if (isDuplicate) throw new DuplicatedException(CustomerMessage.CUSTOMER_EXIST);
    }

    @Override
    public List<CustomerDTO> getAll() throws NoContentException {
        List<CustomerDTO> customerList = new ArrayList<>();

        customerRepository.findAll().forEach(customer -> customerList.add(EntityToDto.customerToDto(customer)));

        if (customerList.isEmpty()) throw new NoContentException(CustomerMessage.EMPTY_LIST);

        return customerList;
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
    public CustomerDTO create(CustomerDTO customerDTO) throws InValidException {
        CustomerValidator.validatorCustomerDTO(customerDTO);

        checkDuplicated(customerDTO, customerRepository.findAll());

        //    private final ModelMapper mapper;
        Customer customer = Customer.builder()
                .firstName(customerDTO.getFirstName())
                .lastName(customerDTO.getLastName())
                .address(customerDTO.getAddress())
                .status(DEFAULT_STATUS)
                .age(Integer.parseInt(customerDTO.getAge()))
                .build();

        customerRepository.save(customer);

        return customerDTO;
    }

    @Override
    public void loadCustomers(MultipartFile file) throws InValidException {
        FileValidator.validatorMultipartFile(file);

        try {
            List<CustomerDTO> customerListToAdd = CsvHelper.csvToCustomers(file.getInputStream(),
                    customerRepository.findAll());

            for (CustomerDTO customerDTO : customerListToAdd) {
                Customer customerToAdd = Customer.builder()
                        .firstName(customerDTO.getFirstName())
                        .lastName(customerDTO.getLastName())
                        .address(customerDTO.getAddress())
                        .status(DEFAULT_STATUS)
                        .age(Integer.parseInt(customerDTO.getAge()))
                        .build();

                customerRepository.save(customerToAdd);
            }

        } catch (IOException e) {
            throw new InValidException(FileMessage.WRONG_WHEN_READ_FILE);
        }
    }

    @Override
    public CustomerDTO update(String id, CustomerUpdateDTO customerToUpdate) {
        CustomerValidator.validatorCustomerUpdateDTO(customerToUpdate);

        Customer storedCustomer = getCustomer(id);

        Customer tempCustomer = Customer.builder()
                .address(storedCustomer.getAddress())
                .firstName(storedCustomer.getFirstName())
                .lastName(storedCustomer.getLastName())
                .age(storedCustomer.getAge())
                .build();

        if (customerToUpdate.getFirstName() != null) {
            tempCustomer.setFirstName(customerToUpdate.getFirstName());
        }
        if (customerToUpdate.getLastName() != null) {
            tempCustomer.setLastName(customerToUpdate.getLastName());
        }
        if (customerToUpdate.getAddress() != null) {
            tempCustomer.setAddress(customerToUpdate.getAddress());
        }
        if (customerToUpdate.getAge() != null) {
            tempCustomer.setAge(Integer.parseInt(customerToUpdate.getAge()));
        }

        checkDuplicated(EntityToDto.customerToDto(tempCustomer), customerRepository.findAll());

        storedCustomer.setAddress(tempCustomer.getAddress());
        storedCustomer.setFirstName(tempCustomer.getFirstName());
        storedCustomer.setLastName(tempCustomer.getLastName());
        storedCustomer.setAge(tempCustomer.getAge());

        customerRepository.save(storedCustomer);

        return EntityToDto.customerToDto(storedCustomer);
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
