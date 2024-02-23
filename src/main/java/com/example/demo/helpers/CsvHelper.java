package com.example.demo.helpers;

import com.example.demo.exception.InValidException;
import com.example.demo.message.FileMessage;
import com.example.demo.model.DTO.ApartmentDTO;
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
    public static final String TYPE = "text/csv";
    public static final String[] CUSTOMER_HEADER = {"First Name", "Last Name", "Address", "Age"};
    public static final String[] APARTMENT_HEADER = {"Address", "Number of room", "Retail price"};

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

            boolean isInvalidHeader = false;

            if (csvParser.getHeaderMap().size() != CUSTOMER_HEADER.length) isInvalidHeader = true;
            else {
                for (String key : CUSTOMER_HEADER) {
                    if (!csvParser.getHeaderMap().containsKey(key)) {
                        isInvalidHeader = true;
                        break;
                    }
                }
            }

            if (isInvalidHeader)
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

    public static List<ApartmentDTO> csvToApartments(MultipartFile file) throws InValidException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(),
                StandardCharsets.UTF_8))) {

            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            boolean isInvalidHeader = false;

            if (csvParser.getHeaderMap().size() != APARTMENT_HEADER.length) isInvalidHeader = true;
            else {
                for (String key : APARTMENT_HEADER) {
                    if (!csvParser.getHeaderMap().containsKey(key)) {
                        isInvalidHeader = true;
                        break;
                    }
                }
            }

            if (isInvalidHeader)
                throw new InValidException(FileMessage.HEADER_MISSING + " (File name: " + file.getOriginalFilename() +
                        ", Actual: " + csvParser.getHeaderMap().keySet() + ", Expected: " + Arrays.toString(CUSTOMER_HEADER) + ")");

            List<ApartmentDTO> apartmentList = new ArrayList<>();

            List<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord record : csvRecords) {
                ApartmentDTO apartmentDTO = ApartmentDTO.builder()
                        .address(record.get(APARTMENT_HEADER[0]))
                        .numberOfRoom(record.get(APARTMENT_HEADER[1]))
                        .retailPrice(record.get(APARTMENT_HEADER[2]))
                        .build();

                apartmentList.add(apartmentDTO);
            }

            return apartmentList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
