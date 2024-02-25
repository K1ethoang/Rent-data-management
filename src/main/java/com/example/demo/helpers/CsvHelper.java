package com.example.demo.helpers;

import com.example.demo.exception.InValidException;
import com.example.demo.message.FileMessage;
import com.example.demo.model.DTO.ApartmentDTO;
import com.example.demo.model.DTO.ContractDTO;
import com.example.demo.model.DTO.CustomerDTO;
import com.example.demo.utils.MyUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class CsvHelper {
    public static final String TYPE = "text/csv";

    // Header row
    public static final String[] CUSTOMER_HEADER = {"First Name", "Last Name", "Address", "Age"};
    public static final String[] APARTMENT_HEADER = {"Address", "Number of room", "Retail price"};
    public static final String[] CONTRACT_HEADER = {"Customer ID", "Apartment ID", "Start Date",
            "End Date"};

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
                        ", Actual: " + csvParser.getHeaderMap().keySet() + ", Expected: " + Arrays.toString(APARTMENT_HEADER) + ")");

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

    public static List<ContractDTO> csvToContracts(MultipartFile file) throws InValidException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(),
                StandardCharsets.UTF_8))) {

            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            boolean isInvalidHeader = false;

            if (csvParser.getHeaderMap().size() != CONTRACT_HEADER.length) isInvalidHeader = true;
            else {
                for (String key : CONTRACT_HEADER) {
                    if (!csvParser.getHeaderMap().containsKey(key)) {
                        isInvalidHeader = true;
                        break;
                    }
                }
            }

            if (isInvalidHeader)
                throw new InValidException(FileMessage.HEADER_MISSING + " (File name: " + file.getOriginalFilename() +
                        ", Actual: " + csvParser.getHeaderMap().keySet() + ", Expected: " + Arrays.toString(CONTRACT_HEADER) + ")");

            List<ContractDTO> contractList = new ArrayList<>();

            List<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord record : csvRecords) {
                ContractDTO contractDTO = ContractDTO.builder()
                        .customerId(record.get(CONTRACT_HEADER[0]))
                        .apartmentId(record.get(CONTRACT_HEADER[1]))
                        .startDate(record.get(CONTRACT_HEADER[2]))
                        .endDate(record.get(CONTRACT_HEADER[3]))
                        .build();

                contractList.add(contractDTO);
            }

            return contractList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static File exportCustomers(List<CustomerDTO> customerList) throws Exception {
        final String NAME_FILE = "customer_" + MyUtils.getDateNow() +
                ".csv";

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(NAME_FILE), CSVFormat.EXCEL)) {
            printer.printRecord(Arrays.stream(CUSTOMER_HEADER).toArray());

            for (CustomerDTO customerDTO : customerList) {
                List<Object> row = new ArrayList<>();

                row.add(customerDTO.getFirstName());
                row.add(customerDTO.getLastName());
                row.add(customerDTO.getAddress());
                row.add(customerDTO.getAge());

                printer.printRecord(row);
            }

            return new File(NAME_FILE);
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public static File getTemplateCustomer() throws Exception {
        final String NAME_FILE = "customer.csv";

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(NAME_FILE), CSVFormat.EXCEL)) {
            printer.printRecord(Arrays.stream(CUSTOMER_HEADER).toArray());

            List<Object> row = new ArrayList<>();

            row.add("Kiet");
            row.add("Hoang");
            row.add("TP. HCM");
            row.add(21);

            printer.printRecord(row);

            return new File(NAME_FILE);
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

}
