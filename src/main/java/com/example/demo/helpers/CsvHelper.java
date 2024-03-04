package com.example.demo.helpers;

import com.example.demo.exception.InValidException;
import com.example.demo.message.FileMessage;
import com.example.demo.model.DTO.apartment.ApartmentDTO;
import com.example.demo.model.DTO.contract.ContractDTO;
import com.example.demo.model.DTO.customer.CustomerDTO;
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

    public static File exportCustomers(List<CustomerDTO> customerList, boolean getTemplate) throws Exception {
        final String NAME_FILE = "customer_" + MyUtils.getDateNow() +
                ".csv";

        final String NAME_FILE_TEMPLATE = "customer_template.csv";

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(getTemplate ? NAME_FILE_TEMPLATE
                : NAME_FILE),
                CSVFormat.EXCEL)) {
            printer.printRecord(Arrays.stream(CUSTOMER_HEADER).toArray());

            List<Object> row = new ArrayList<>();

            if (getTemplate) {
                row.add("<First name>");
                row.add("<Last name>");
                row.add("<Address>");
                row.add("<Age>");

                printer.printRecord(row);
            } else {
                for (CustomerDTO customerDTO : customerList) {
                    row.add(customerDTO.getFirstName());
                    row.add(customerDTO.getLastName());
                    row.add(customerDTO.getAddress());
                    row.add(customerDTO.getAge());

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


            printer.printRecord(Arrays.stream(APARTMENT_HEADER).toArray());

            List<Object> row = new ArrayList<>();

            if (getTemplate) {
                row.add("<Address>");
                row.add("<Numbers of room>");
                row.add("<Price>");

                printer.printRecord(row);
            } else {
                for (ApartmentDTO apartmentDTO : apartmentList) {
                    row.add(apartmentDTO.getAddress());
                    row.add(apartmentDTO.getNumberOfRoom());
                    row.add(apartmentDTO.getRetailPrice());

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

    public static File exportContracts(List<ContractDTO> contractList, boolean getTemplate) throws Exception {
        final String NAME_FILE = "contract_" + MyUtils.getDateNow() +
                ".csv";

        final String NAME_FILE_TEMPLATE = "contract_template.csv";

        try (CSVPrinter printer = new CSVPrinter(new FileWriter(getTemplate ? NAME_FILE_TEMPLATE
                : NAME_FILE),
                CSVFormat.EXCEL)) {
            printer.printRecord(Arrays.stream(CONTRACT_HEADER).toArray());

            List<Object> row = new ArrayList<>();

            if (getTemplate) {
                row.add("<Customer ID>");
                row.add("<Apartment ID>");
                row.add("<YYYY-MM-DD>");
                row.add("<YYYY-MM-DD>");

                printer.printRecord(row);
            } else {
                for (ContractDTO contract : contractList) {
                    row.add(contract.getCustomerId());
                    row.add(contract.getApartmentId());
                    row.add(contract.getStartDate());
                    row.add(contract.getEndDate());

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
