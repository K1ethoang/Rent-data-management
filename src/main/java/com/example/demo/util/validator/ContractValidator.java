package com.example.demo.util.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotNullException;
import com.example.demo.message.ContractMessage;
import com.example.demo.model.DTO.contract.ContractDTO;
import com.example.demo.model.DTO.contract.ContractUpdateDTO;
import com.example.demo.util.MyUtils;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.util.Objects;

@Log4j2
public class ContractValidator {
    public static void notNullStartDate(String startDate) throws NotNullException {
        if (startDate == null || startDate.trim().isEmpty())
            throw new NotNullException(ContractMessage.NOT_NULL_START_DATE);
    }

    public static void notNullEndDate(String endDate) throws NotNullException {
        if (endDate == null || endDate.trim().isEmpty())
            throw new NotNullException(ContractMessage.NOT_NULL_END_DATE);
    }

    public static void notNullCustomerId(String customerId) throws NotNullException {
        if (customerId == null || customerId.trim().isEmpty())
            throw new NotNullException(ContractMessage.NOT_NULL_CUSTOMER_ID);
    }

    public static void notNullApartmentId(String apartmentId) throws NotNullException {
        if (apartmentId == null || apartmentId.trim().isEmpty())
            throw new NotNullException(ContractMessage.NOT_NULL_APARTMENT_ID);
    }

    public static void invalidStartDate(String startDate) throws InValidException {
        if (MyUtils.stringToDate(startDate.trim()) == null)
            throw new InValidException(ContractMessage.INVALID_START_DATE);
    }

    public static void invalidEndDate(String endDate) throws InValidException {
        if (MyUtils.stringToDate(endDate.trim()) == null)
            throw new InValidException(ContractMessage.INVALID_END_DATE);
    }

    public static void invalidStartDateAndEndDate(LocalDate startDate, LocalDate endDate) throws InValidException {
        if (startDate.isAfter(endDate) || startDate.isEqual(endDate))
            throw new InValidException(ContractMessage.START_DATE_LESS_THAN_END_DATE);
    }

    public static void validatorContractDTO(ContractDTO contractDTO) {
        notNullStartDate(contractDTO.getStartDate());
        invalidStartDate(contractDTO.getStartDate());
        notNullEndDate(contractDTO.getEndDate());
        invalidEndDate(contractDTO.getEndDate());
        invalidStartDateAndEndDate(Objects.requireNonNull(MyUtils.stringToDate(contractDTO.getStartDate())), Objects.requireNonNull(MyUtils.stringToDate(contractDTO.getEndDate())));
        notNullCustomerId(contractDTO.getCustomerId());
        notNullApartmentId(contractDTO.getApartmentId());

        contractDTO.setStartDate(contractDTO.getStartDate().trim());
        contractDTO.setEndDate(contractDTO.getEndDate().trim());
        contractDTO.setApartmentId(contractDTO.getApartmentId().trim());
        contractDTO.setCustomerId(contractDTO.getCustomerId().trim());
    }

    public static void validatorContactUpdateDTO(ContractUpdateDTO contractUpdateDTO) {
        if (contractUpdateDTO.getStartDate() != null) {
            notNullStartDate(contractUpdateDTO.getStartDate());
            invalidStartDate(contractUpdateDTO.getStartDate());
            contractUpdateDTO.setStartDate(contractUpdateDTO.getStartDate().trim());
        }

        if (contractUpdateDTO.getEndDate() != null) {
            notNullEndDate(contractUpdateDTO.getEndDate());
            invalidEndDate(contractUpdateDTO.getEndDate());
            contractUpdateDTO.setEndDate(contractUpdateDTO.getEndDate().trim());
        }

        if (contractUpdateDTO.getStartDate() != null && contractUpdateDTO.getEndDate() != null) {
            invalidStartDateAndEndDate(Objects.requireNonNull(MyUtils.stringToDate(contractUpdateDTO.getStartDate())), Objects.requireNonNull(MyUtils.stringToDate(contractUpdateDTO.getEndDate())));
        }

        if (contractUpdateDTO.getCustomerId() != null) {
            notNullCustomerId(contractUpdateDTO.getCustomerId());
            contractUpdateDTO.setCustomerId(contractUpdateDTO.getCustomerId().trim());
        }

        if (contractUpdateDTO.getApartmentId() != null) {
            notNullApartmentId(contractUpdateDTO.getApartmentId());
            contractUpdateDTO.setApartmentId(contractUpdateDTO.getApartmentId().trim());
        }
    }

}
