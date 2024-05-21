package com.example.demo.service.implement;

import com.example.demo.entity.Customer;
import com.example.demo.exception.DuplicatedException;
import com.example.demo.exception.InValidException;
import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.helper.CsvHelper;
import com.example.demo.message.CustomerMessage;
import com.example.demo.message.FileMessage;
import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.customer.CustomerDTO;
import com.example.demo.model.DTO.customer.CustomerUpdateDTO;
import com.example.demo.model.DTO.paging.APIPageableDTO;
import com.example.demo.model.mapper.EntityToDto;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.interfaces.CustomerService;
import com.example.demo.util.validator.CustomerValidator;
import com.example.demo.util.validator.FileValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
@Log4j2
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;


    @Override
    public Map<String, Object> getAll(Pageable pageable) throws NoContentException {
        Map<String, Object> result = new HashMap<>();

        Page<Customer> pageEntity =
                customerRepository.findAll(pageable);
        Page<CustomerDTO> PageDTOS = pageEntity.map(EntityToDto::customerToDto);

        APIPageableDTO pageableResult = new APIPageableDTO(PageDTOS);

        result.put("page", pageableResult);
        result.put("customers", PageDTOS.getContent());

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

        Customer customer = Customer.builder()
                .fullName(customerDTO.getFullName())
                .citizenId(customerDTO.getCitizenId())
                .address(customerDTO.getAddress())
                .dob(LocalDate.parse(customerDTO.getDob()))
                .phoneNumber(customerDTO.getPhoneNumber())
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

        CustomerDTO tempCustomer = EntityToDto.customerToDto(storedCustomer);
        log.info(tempCustomer);
        log.info(customerToUpdate);

        if (customerToUpdate.getFullName() != null) {
            tempCustomer.setFullName(customerToUpdate.getFullName());
        }
        if (customerToUpdate.getDob() != null) {
            tempCustomer.setDob(customerToUpdate.getDob());
        }
        if (customerToUpdate.getAddress() != null) {
            tempCustomer.setAddress(customerToUpdate.getAddress());
        }
        if (customerToUpdate.getCitizenId() != null) {
            tempCustomer.setCitizenId(customerToUpdate.getCitizenId());
        }
        if (customerToUpdate.getPhoneNumber() != null) {
            tempCustomer.setPhoneNumber(customerToUpdate.getPhoneNumber());
        }

        checkDuplicated(tempCustomer);

        log.info(tempCustomer);
        log.info(customerToUpdate);

        storedCustomer.setFullName(tempCustomer.getFullName());
        storedCustomer.setAddress(tempCustomer.getAddress());
        storedCustomer.setPhoneNumber(tempCustomer.getPhoneNumber());
        storedCustomer.setCitizenId(tempCustomer.getCitizenId());
        storedCustomer.setDob(LocalDate.parse(tempCustomer.getDob()));

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

        for (Customer customer : customerList) {
            // kiểm tra xem có phải customer đang check có trong danh sách không
            if (customerToCheck.getId() != null && customerToCheck.getId().equals(customer.getId()))
                continue;

            CustomerDTO customerFromList = EntityToDto.customerToDto(customer);

            if (customerFromList.getPhoneNumber().equals(customerToCheck.getPhoneNumber())) {
                throw new DuplicatedException(CustomerMessage.DUPLICATED_PHONE_NUMBER);
            }
            if (customerFromList.getCitizenId().equals(customerToCheck.getCitizenId())) {
                throw new DuplicatedException(CustomerMessage.DUPLICATED_CITIZEN_ID);
            }
        }
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
                    .fullName(customerList.get(i).getFullName())
                    .citizenId(customerList.get(i).getCitizenId())
                    .dob(LocalDate.parse(customerList.get(i).getDob()))
                    .phoneNumber(customerList.get(i).getPhoneNumber())
                    .address(customerList.get(i).getAddress())
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
            List<CustomerDTO> customerDTOS = new ArrayList<>();
            customerRepository.findAll().forEach(customer -> customerDTOS.add(EntityToDto.customerToDto(customer)));
            return CsvHelper.exportCustomers(customerDTOS, getTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> search(String query, Pageable pageable) throws InValidException {
        if (query == null || query.trim().isEmpty())
            throw new InValidException(GlobalMessage.NOT_NULL_QUERY);

        Map<String, Object> result = new HashMap<>();

        Page<Customer> pageEntity = customerRepository.search(query.trim(), pageable);
        Page<CustomerDTO> pageDTOS = pageEntity.map(EntityToDto::customerToDto);

        APIPageableDTO pageableResult = new APIPageableDTO(pageDTOS);

        result.put("page", pageableResult);
        result.put("customers", pageDTOS.getContent());

        return result;
    }

    @Override
    public Map<String, Object> statistic() {
        Map<String, Object> res = new HashMap<>();

        long count = customerRepository.count();

        if (count == 0) return null;

        res.put("count", count);

        return res;
    }
}
