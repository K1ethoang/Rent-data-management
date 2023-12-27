package com.example.demo.model.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotNullException;
import com.example.demo.helpers.Helper;
import com.example.demo.message.ContractMessage;
import com.example.demo.model.DTO.ContractDTO;
import com.example.demo.utils.MyUtils;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;

@Log4j2
public class ContractValidator {
    public static void notNullStartDate(String startDate) throws NotNullException {
        if (MyUtils.isNull(startDate)) return;
        if (MyUtils.isEmpty(startDate)) throw new NotNullException(ContractMessage.NOT_NULL_START_DATE);
    }

    public static void notNullEndDate(String endDate) throws NotNullException {
        if (MyUtils.isNull(endDate)) return;
        if (MyUtils.isEmpty(endDate)) throw new NotNullException(ContractMessage.NOT_NULL_END_DATE);
    }

    public static void notNullCustomerId(String customerId) throws NotNullException {
        if (MyUtils.isNull(customerId)) return;
        if (MyUtils.isEmpty(customerId)) throw new NotNullException(ContractMessage.NOT_NULL_CUSTOMER_ID);
    }

    public static void notNullApartmentId(String apartmentId) throws NotNullException {
        if (MyUtils.isNull(apartmentId)) return;
        if (MyUtils.isEmpty(apartmentId)) throw new NotNullException(ContractMessage.NOT_NULL_APARTMENT_ID);
    }

    public static void invalidStartDate(String startDate) throws InValidException {
        if (MyUtils.isNull(startDate) || MyUtils.isEmpty(startDate)) return;
        if (!MyUtils.isValidDate(startDate)) throw new InValidException(ContractMessage.INVALID_START_DATE);
    }

    public static void invalidEndDate(String endDate) throws InValidException {
        if (MyUtils.isNull(endDate) || MyUtils.isEmpty(endDate)) return;
        if (!MyUtils.isValidDate(endDate)) throw new InValidException(ContractMessage.INVALID_END_DATE);
    }

    public static void invalidStartDateAndEndDate(String startDate, String endDate) throws InValidException {
        invalidStartDate(startDate);
        invalidEndDate(endDate);

        LocalDate startD = MyUtils.stringToDate(startDate);
        LocalDate endD = MyUtils.stringToDate(endDate);

        if (startD.isAfter(endD) || startD.isEqual(endD))
            throw new InValidException(ContractMessage.START_DATE_LESS_THAN_END_DATE);
    }

    public static void validator(ContractDTO contractDTO) {
        Helper.trimToSetAllField(contractDTO);

        notNullStartDate(contractDTO.getStartDate());
        notNullEndDate(contractDTO.getEndDate());
        notNullCustomerId(contractDTO.getCustomerId());
        notNullApartmentId(contractDTO.getApartmentId());

        invalidStartDateAndEndDate(contractDTO.getStartDate(), contractDTO.getEndDate());
    }
}
