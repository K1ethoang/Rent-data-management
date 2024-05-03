//package com.example.demo.helper;
//
//import com.example.demo.exception.InValidException;
//import com.example.demo.message.FileMessage;
//import com.example.demo.model.DTO.customer.CustomerDTO;
//import lombok.extern.log4j.Log4j2;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.*;
//
//@Log4j2
//public class ExcelHelper {
//
//    public static String xlsxType = ".xlsx";
//    public static String xlsType = ".xls";
//
//    public static List<CustomerDTO> excelToCustomers(MultipartFile file) {
//        List<CustomerDTO> customerList = new ArrayList<>();
//
//        // Get workbook
//        try {
//            Workbook workbook = getWorkbook(file);
//
//            // Get sheet
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // Get all rows
//            for (Row nextRow : sheet) {
//                if (nextRow.getRowNum() == 0) {
//                    // Check header
//                    Iterator<Cell> cellIterator = nextRow.cellIterator();
//
//                    while (cellIterator.hasNext()) {
//                        Cell cell = cellIterator.next();
//
//                        Object cellValue = getCellValue(cell);
//                        if (cellValue == null || cellValue.toString().isEmpty() || !cellValue.toString().equalsIgnoreCase(CsvHelper.CUSTOMER_HEADER[cell.getColumnIndex()])) {
//                            throw new InValidException(FileMessage.HEADER_MISSING + " (File name:" +
//                                    " " + file.getOriginalFilename() +
//                                    ", Expected header: " + Arrays.toString(CsvHelper.CUSTOMER_HEADER) + ")");
//                        }
//                    }
//                    continue;
//                }
//
//                // Get all cells
//                Iterator<Cell> cellIterator = nextRow.cellIterator();
//
//                // Read cells and set value for object
//                CustomerDTO customerDTO = new CustomerDTO();
//                while (cellIterator.hasNext()) {
//                    //Read cell
//                    Cell cell = cellIterator.next();
//                    Object cellValue = getCellValue(cell);
//                    if (cellValue == null || cellValue.toString().isEmpty()) {
//                        continue;
//                    }
//                    // Set value for object
//                    int columnIndex = cell.getColumnIndex();
//                    switch (columnIndex) {
//                        case 0:
//                            customerDTO.setFirstName((String) getCellValue(cell));
//                            break;
//                        case 1:
//                            customerDTO.setLastName((String) getCellValue(cell));
//                            break;
//                        case 2:
//                            customerDTO.setAddress((String) getCellValue(cell));
//                            break;
//                        case 3:
//                            customerDTO.setAge(String.valueOf((int) getCellValue(cell)));
//                            break;
//                        default:
//                            break;
//                    }
//
//                }
//                customerList.add(customerDTO);
//            }
//
//            workbook.close();
//            return customerList;
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//    private static Workbook getWorkbook(MultipartFile file) throws Exception {
//        Workbook workbook = null;
//        try {
//            if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(xlsxType)) {
//                workbook = new XSSFWorkbook(file.getInputStream());
//            } else if (file.getOriginalFilename().endsWith(xlsType)) {
//                workbook = new HSSFWorkbook(file.getInputStream());
//            } else {
//                throw new InValidException(FileMessage.MUST_TYPE_EXCEL);
//            }
//
//            return workbook;
//        } catch (IOException e) {
//            throw new Exception(e.getMessage());
//        }
//    }
//
//    // Get cell value
//    private static Object getCellValue(Cell cell) {
//        CellType cellType = cell.getCellType();
//        Object cellValue = null;
//
//        switch (cellType) {
//            case BOOLEAN:
//                cellValue = cell.getBooleanCellValue();
//                break;
//            case FORMULA:
//                Workbook workbook = cell.getSheet().getWorkbook();
//                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//                cellValue = evaluator.evaluate(cell).getNumberValue();
//                break;
//            case NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    cellValue = cell.getDateCellValue();
//                } else {
//                    cellValue = cell.getNumericCellValue();
//                }
//                break;
//            case STRING:
//                cellValue = cell.getRichStringCellValue().getString();
//                break;
//            default:
//                break;
//        }
//
//        return cellValue;
//    }
//}
