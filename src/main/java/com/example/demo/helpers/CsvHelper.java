package com.example.demo.helpers;

import com.example.demo.entity.Customer;
import com.example.demo.exception.DuplicatedException;
import com.example.demo.model.DTO.CustomerDTO;
import com.example.demo.service.implement.CustomerServiceImp;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class CsvHelper {
    private static final String TYPE = "text/csv";
    // Header row
    private static final String[] CUSTOMER_HEADER = {"First Name", "Last Name", "Address", "Age"};

    public static boolean hasCsvFormat(MultipartFile file) {
        return (TYPE.equals(file.getContentType()));
    }

    public static List<CustomerDTO> csvToCustomers(InputStream is, List<Customer> customerList) {
        try (BufferedReader fileReader =
                     new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            CSVParser csvParser = new CSVParser(fileReader,
                    CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            List<CustomerDTO> customerListToAdd = new ArrayList<>();

            List<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord record : csvRecords) {
                CustomerDTO customerDTO = CustomerDTO.builder()
                        .firstName(record.get(CUSTOMER_HEADER[0]))
                        .lastName(record.get(CUSTOMER_HEADER[1]))
                        .address(record.get(CUSTOMER_HEADER[2]))
                        .age(record.get(CUSTOMER_HEADER[3]))
                        .build();

                try {
                    CustomerServiceImp.checkDuplicated(customerDTO, customerList);
                } catch (DuplicatedException e) {
                    log.info(customerDTO);
                    continue;
                }

                customerListToAdd.add(customerDTO);
            }
            return customerListToAdd;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
