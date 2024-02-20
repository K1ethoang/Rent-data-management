package com.example.demo.helpers;

import com.example.demo.exception.InValidException;
import com.example.demo.message.FileMessage;
import com.example.demo.model.DTO.CustomerDTO;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class CsvHelper {
    // Header row
    public static final String[] CUSTOMER_HEADER = {"First Name", "Last Name", "Address", "Age"};
    public static final String TYPE = "text/csv";

    public static boolean hasCsvFormat(MultipartFile file) {
        return (TYPE.equals(file.getContentType()));
    }

    public static List<CustomerDTO> csvToCustomers(MultipartFile file) throws InValidException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(),
                StandardCharsets.UTF_8))) {

            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            if (csvParser.getHeaderMap().size() != CUSTOMER_HEADER.length)
                throw new InValidException(FileMessage.HEADER_MISSING + " (File name: " + file.getOriginalFilename() +
                        ", Actual: " + csvParser.getHeaderMap().keySet() + ", Expected: " + Arrays.toString(CUSTOMER_HEADER) + ")");

            List<CustomerDTO> customerList = new ArrayList<>();

            List<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord record : csvRecords) {
                try {
                    CustomerDTO customerDTO = CustomerDTO.builder()
                            .firstName(record.get(CUSTOMER_HEADER[0]))
                            .lastName(record.get(CUSTOMER_HEADER[1]))
                            .address(record.get(CUSTOMER_HEADER[2]))
                            .age(record.get(CUSTOMER_HEADER[3]))
                            .build();

                    customerList.add(customerDTO);
                } catch (RuntimeException e) {
                    throw new InValidException(FileMessage.HEADER_MISSING + " (File name: " + file.getOriginalFilename() +
                            ", Actual: " + csvParser.getHeaderMap().keySet() + ", Expected: " + Arrays.toString(CUSTOMER_HEADER) + ")");
                }
            }

            return customerList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
