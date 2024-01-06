package com.example.demo.utils.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotNullException;
import com.example.demo.message.ContractMessage;
import com.example.demo.model.DTO.ContractDTO;
import com.example.demo.model.DTO.ContractUpdateDTO;
import com.example.demo.utils.MyUtils;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;

@Log4j2
public class ContractValidator {
    private static void notNullStartDate(String startDate) throws NotNullException {
        if (startDate == null || startDate.trim().isEmpty())
            throw new NotNullException(ContractMessage.NOT_NULL_START_DATE);
    }

    private static void notNullEndDate(String endDate) throws NotNullException {
        if (endDate == null || endDate.trim().isEmpty()) throw new NotNullException(ContractMessage.NOT_NULL_END_DATE);
    }

    private static void notNullCustomerId(String customerId) throws NotNullException {
        if (customerId == null || customerId.trim().isEmpty())
            throw new NotNullException(ContractMessage.NOT_NULL_CUSTOMER_ID);
    }

    private static void notNullApartmentId(String apartmentId) throws NotNullException {
        if (apartmentId == null || apartmentId.trim().isEmpty())
            throw new NotNullException(ContractMessage.NOT_NULL_APARTMENT_ID);
    }

    private static void invalidStartDate(String startDate) throws InValidException {
        if (!MyUtils.isValidDate(startDate)) throw new InValidException(ContractMessage.INVALID_START_DATE);
    }

    private static void invalidEndDate(String endDate) throws InValidException {
        if (!MyUtils.isValidDate(endDate)) throw new InValidException(ContractMessage.INVALID_END_DATE);
    }

    public static void invalidStartDateAndEndDate(String startDate, String endDate) throws InValidException {
        LocalDate startD = MyUtils.stringToDate(startDate);
        LocalDate endD = MyUtils.stringToDate(endDate);

        if (startD.isAfter(endD) || startD.isEqual(endD))
            throw new InValidException(ContractMessage.START_DATE_LESS_THAN_END_DATE);
    }

    public static void validatorContractDTO(ContractDTO contractDTO) {
        notNullStartDate(contractDTO.getStartDate());
        invalidStartDate(contractDTO.getStartDate());
        notNullEndDate(contractDTO.getEndDate());
        invalidEndDate(contractDTO.getEndDate());
        invalidStartDateAndEndDate(contractDTO.getStartDate(), contractDTO.getEndDate());
        notNullCustomerId(contractDTO.getCustomerId());
        notNullApartmentId(contractDTO.getApartmentId());
    }

    public static void validatorContactUpdateDTO(ContractUpdateDTO contractUpdateDTO) {
        if (contractUpdateDTO.getStartDate() != null) {
            notNullStartDate(contractUpdateDTO.getStartDate());
            invalidStartDate(contractUpdateDTO.getStartDate());
        }

        if (contractUpdateDTO.getEndDate() != null) {
            notNullEndDate(contractUpdateDTO.getEndDate());
            invalidEndDate(contractUpdateDTO.getEndDate());
        }

        if (contractUpdateDTO.getStartDate() != null && contractUpdateDTO.getEndDate() != null) {
            invalidStartDateAndEndDate(contractUpdateDTO.getStartDate(), contractUpdateDTO.getEndDate());
        }

        if (contractUpdateDTO.getCustomerId() != null) {
            notNullCustomerId(contractUpdateDTO.getCustomerId());
        }

        if (contractUpdateDTO.getApartmentId() != null) {
            notNullApartmentId(contractUpdateDTO.getApartmentId());
        }
    }

}
