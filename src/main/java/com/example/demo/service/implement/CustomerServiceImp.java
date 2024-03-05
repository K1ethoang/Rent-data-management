package com.example.demo.service.implement;

import com.example.demo.entity.Customer;
import com.example.demo.exception.DuplicatedException;
import com.example.demo.exception.InValidException;
import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.helpers.CsvHelper;
import com.example.demo.message.CustomerMessage;
import com.example.demo.message.FileMessage;
import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.customer.CustomerDTO;
import com.example.demo.model.DTO.customer.CustomerUpdateDTO;
import com.example.demo.model.DTO.paging.APIPageableDTO;
import com.example.demo.model.mapper.EntityToDto;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.interfaces.CustomerService;
import com.example.demo.utils.MyUtils;
import com.example.demo.utils.validator.CustomerValidator;
import com.example.demo.utils.validator.FileValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
@AllArgsConstructor
@Log4j2
public class CustomerServiceImp implements CustomerService {
    public static final String DEFAULT_STATUS = "Normal";
    private final CustomerRepository customerRepository;


    @Override
    public Map<String, Object>  getAll(Pageable pageable) throws NoContentException {
        Map<String, Object> result = new HashMap<>();

        Page<CustomerDTO> page =
                customerRepository.findAll(pageable).map(EntityToDto::customerToDto);

        APIPageableDTO pageableResult = new APIPageableDTO(page);

        result.put("page", pageableResult);
        result.put("customers", page.getContent());

        return result;
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

        checkDuplicated(customerDTO);

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
    public List<Object> loadCustomers(MultipartFile[] files) throws InValidException {
        List<Object> response = new ArrayList<>();

        for (MultipartFile file : files) {
            response.add(loadCustomer(file));
        }

        return response;
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

        checkDuplicated(EntityToDto.customerToDto(tempCustomer));

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

    public void checkDuplicated(CustomerDTO customerToCheck) throws DuplicatedException {
        List<Customer> customerList = customerRepository.findAll();

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
    public Map<String, Object> loadCustomer(MultipartFile file) throws InValidException {
        FileValidator.validatorMultipartFile(file);

        Map<String, Object> response = new HashMap<>();
        response.put("File", file.getOriginalFilename());

        List<CustomerDTO> customerList = null;

        if (CsvHelper.hasCsvFormat(file)) customerList = CsvHelper.csvToCustomers(file);
//        else customerList = ExcelHelper.excelToCustomers(file);

        int numberOfCustomerAdded = 0;

        String failedRows = "";

        for (int i = 0; i < customerList.size(); i++) {
            try {
                CustomerValidator.validatorCustomerDTO(customerList.get(i));
            } catch (Exception e) {
                failedRows = failedRows.concat((i + 1) + " , ");
                continue;
            }

            try {
                checkDuplicated(customerList.get(i));
            } catch (DuplicatedException e) {
                failedRows = failedRows.concat((i + 1) + " , ");
                continue;
            }

            Customer customerToAdd = Customer.builder()
                    .firstName(customerList.get(i).getFirstName())
                    .lastName(customerList.get(i).getLastName())
                    .address(customerList.get(i).getAddress())
                    .status(CustomerServiceImp.DEFAULT_STATUS)
                    .age(Integer.parseInt(customerList.get(i).getAge()))
                    .build();

            customerRepository.save(customerToAdd);
            numberOfCustomerAdded++;
        }

        response.put(FileMessage.NUMBER_SUCCESS_ROW, numberOfCustomerAdded);

        if (!failedRows.isEmpty())
            failedRows = failedRows.substring(0, failedRows.length() - 3);
        response.put(FileMessage.FAILED_ROWS, failedRows);

        return response;
    }

    @Override
    public File exportCsv(boolean getTemplate) {
        try {
            List<CustomerDTO> customerList = new ArrayList<>();
            customerRepository.findAll().forEach(customer -> customerList.add(EntityToDto.customerToDto(customer)));

            return CsvHelper.exportCustomers(customerList, getTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> search(String query, Pageable pageable) throws InValidException
    {
        if(query==null || query.trim().isEmpty())
            throw new InValidException(GlobalMessage.NOT_NULL_QUERY);

        Map<String, Object> result = new HashMap<>();

        Page<CustomerDTO> page =
                customerRepository.search(query.trim(),pageable).map(EntityToDto::customerToDto);

        APIPageableDTO pageableResult = new APIPageableDTO(page);

        result.put("page", pageableResult);
        result.put("customers", page.getContent());

        return result;
    }
}
