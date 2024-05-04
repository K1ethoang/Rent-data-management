package com.example.demo.helper;

import com.example.demo.exception.InValidException;
import com.example.demo.message.FileMessage;
import com.example.demo.model.DTO.apartment.ApartmentDTO;
import com.example.demo.model.DTO.contract.ContractDTO;
import com.example.demo.model.DTO.customer.CustomerDTO;
import com.example.demo.util.MyUtils;
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
    public static final String[] CUSTOMER_HEADER = {"Full name", "Citizen id", "Address", "Date " +
            "of birth", "Phone number"};
    public static final String[] APARTMENT_HEADER = {"Address", "Number of room", "Retail price"};
    public static final String[] CONTRACT_HEADER = {"Customer ID", "Apartment ID", "Start Date",
            "End Date", "Create date", "Retail price", "Total", "User ID"};

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
                            .fullName(record.get(CUSTOMER_HEADER[0]))
                            .citizenId(record.get(CUSTOMER_HEADER[1]))
                            .address(record.get(CUSTOMER_HEADER[2]))
                            .dob(record.get(CUSTOMER_HEADER[3]))
                            .phoneNumber(record.get(CUSTOMER_HEADER[4]))
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
                        .createDate(record.get(CONTRACT_HEADER[4]))
                        .retailPrice(record.get(CONTRACT_HEADER[5]))
                        .total(record.get(CONTRACT_HEADER[6]))
                        .userId(record.get(CONTRACT_HEADER[7]))
                        .build();

                contractList.add(contractDTO);
            }

            return contractList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static File exportCustomers(List<CustomerDTO> customerList, boolean getTemplate) throws Exception {
        final String NAME_FILE = "customer_" + MyUtils.getDateNow() +
                ".csv";

        final String NAME_FILE_TEMPLATE = "customer_template.csv";

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(getTemplate ? NAME_FILE_TEMPLATE
                : NAME_FILE),
                CSVFormat.EXCEL)) {

            List<Object> row = new ArrayList<>();

            if (getTemplate) {
                printer.printRecord(Arrays.stream(CUSTOMER_HEADER).toArray());

                row.add("Hoàng Gia Kiệt");
                row.add("0123456789012");
                row.add("239 Dien Bien Phu, Di An, Binh Duong");
                row.add("2003-01-12");
                row.add("0892341453");

                printer.printRecord(row);
            } else {
                String[] newHeader = Arrays.copyOf(CUSTOMER_HEADER, CUSTOMER_HEADER.length + 1);
                newHeader[0] = "ID";

                printer.printRecord(Arrays.stream(newHeader).toArray());

                for (CustomerDTO customer : customerList) {
                    row.add(customer.getId());
                    row.add(customer.getFullName());
                    row.add(customer.getCitizenId());
                    row.add(customer.getAddress());
                    row.add(customer.getDob());
                    row.add(customer.getPhoneNumber());

                    printer.printRecord(row);
                    row.clear();
                }
            }

            return new File(getTemplate ? NAME_FILE_TEMPLATE
                    : NAME_FILE);
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public static File exportApartments(List<ApartmentDTO> apartmentList, boolean getTemplate) throws Exception {
        final String NAME_FILE = "apartment_" + MyUtils.getDateNow() +
                ".csv";

        final String NAME_FILE_TEMPLATE = "apartment_template.csv";

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(getTemplate ? NAME_FILE_TEMPLATE
                : NAME_FILE),
                CSVFormat.EXCEL)) {

            List<Object> row = new ArrayList<>();

            if (getTemplate) {
                printer.printRecord(Arrays.stream(APARTMENT_HEADER).toArray());

                row.add("130 Pham Ngu Lao, An Binh, Bien Hoa, Dong Nai");
                row.add("4");
                row.add("3500000");

                printer.printRecord(row);
            } else {
                String[] newHeader = Arrays.copyOf(APARTMENT_HEADER, APARTMENT_HEADER.length + 1);
                newHeader[0] = "ID";

                printer.printRecord(Arrays.stream(newHeader).toArray());

                for (ApartmentDTO apartment : apartmentList) {
                    row.add(apartment.getId());
                    row.add(apartment.getAddress());
                    row.add(apartment.getNumberOfRoom());
                    row.add(apartment.getRetailPrice());

                    printer.printRecord(row);
                    row.clear();
                }
            }

            return new File(getTemplate ? NAME_FILE_TEMPLATE : NAME_FILE);
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public static File exportContracts(List<ContractDTO> contractList, boolean getTemplate) throws Exception {
        final String NAME_FILE = "contract_" + MyUtils.getDateNow() +
                ".csv";

        final String NAME_FILE_TEMPLATE = "contract_template.csv";

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(getTemplate ? NAME_FILE_TEMPLATE
                : NAME_FILE),
                CSVFormat.EXCEL)) {

            List<Object> row = new ArrayList<>();

            if (getTemplate) {
                printer.printRecord(Arrays.stream(CONTRACT_HEADER).toArray());

                row.add("4683fef4-61f1-4186-8fab-be6855f164f5");
                row.add("ea05f3a6-e8f6-4da7-9535-064aeaf2a9f9");
                row.add("2023-04-17");
                row.add("2025-04-17");
                row.add("2025-04-15");
                row.add("2300000");
                row.add("43000000");
                row.add("7c9081fe-41ef-4a6f-89e0-c371afdc694f");

                printer.printRecord(row);
            } else {
                List<String> newHeader = new ArrayList<>(Arrays.asList(CONTRACT_HEADER));
                newHeader.add(0, "ID");

                printer.printRecord(newHeader);
                for (ContractDTO contract : contractList) {
                    row.add(contract.getId());
                    row.add(contract.getCustomerId());
                    row.add(contract.getApartmentId());
                    row.add(contract.getStartDate());
                    row.add(contract.getEndDate());
                    row.add(contract.getCreateDate());
                    row.add(contract.getRetailPrice());
                    row.add(contract.getTotal());
                    row.add(contract.getUserId());

                    printer.printRecord(row);
                    row.clear();
                }
            }

            return new File(getTemplate ? NAME_FILE_TEMPLATE
                    : NAME_FILE);
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
}
