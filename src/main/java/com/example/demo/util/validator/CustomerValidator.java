package com.example.demo.util.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotNullException;
import com.example.demo.message.CustomerMessage;
import com.example.demo.model.DTO.customer.CustomerDTO;
import com.example.demo.model.DTO.customer.CustomerUpdateDTO;
import com.example.demo.util.AuthUtils;
import com.example.demo.util.MyUtils;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;

@Log4j2
public class CustomerValidator {
    private static int LENGTH_OF_CITIZEN_ID = 12;

    private static void notNullFullname(String fullName) throws NotNullException {
        if (fullName == null || fullName.trim().isEmpty())
            throw new NotNullException(CustomerMessage.NOT_NULL_FULL_NAME);
    }

    private static void notNullCitizenId(String citizenId) throws NotNullException {
        if (citizenId == null || citizenId.trim().isEmpty())
            throw new NotNullException(CustomerMessage.NOT_NULL_CITIZEN_ID);
    }

    private static void notNullAddress(String address) throws NotNullException {
        if (address == null || address.trim().isEmpty())
            throw new NotNullException(CustomerMessage.NOT_NULL_ADDRESS);
    }

    private static void notNullDob(String dob) throws NotNullException {
        if (dob == null || dob.trim().isEmpty())
            throw new NotNullException(CustomerMessage.NOT_NULL_DOB);
    }

    private static void notNullPhoneNumber(String phoneNumber) throws NotNullException {
        if (phoneNumber == null || phoneNumber.trim().isEmpty())
            throw new NotNullException(CustomerMessage.NOT_NULL_PHONE_NUMBER);
    }

    private static void invalidDob(String dob) throws InValidException {
        LocalDate dateOfBirth = MyUtils.stringToDate(dob.trim());
        if (dateOfBirth == null || dateOfBirth.equals(LocalDate.now()))
            throw new InValidException(CustomerMessage.INVALID_DOB);
    }

    private static void invalidPhoneNumber(String phoneNumber) throws InValidException {
        if (!AuthUtils.isValidPhoneNumber(phoneNumber))
            throw new InValidException(CustomerMessage.INVALID_PHONE_NUMBER);
    }

    private static void invalidCitizenId(String citizenId) throws InValidException {
        if (!AuthUtils.isValidCitizenId(citizenId))
            throw new InValidException(CustomerMessage.INVALID_CITIZEN_ID);
    }

    public static void validatorCustomerDTO(CustomerDTO customerDTO) {
        notNullFullname(customerDTO.getFullName());
        notNullAddress(customerDTO.getAddress());
        notNullCitizenId(customerDTO.getCitizenId());
        notNullPhoneNumber(customerDTO.getPhoneNumber());
        notNullDob(customerDTO.getDob());

        invalidCitizenId(customerDTO.getCitizenId());
        invalidPhoneNumber(customerDTO.getPhoneNumber());
        invalidDob(customerDTO.getDob());

        customerDTO.setFullName(customerDTO.getFullName().trim());
        customerDTO.setAddress(customerDTO.getAddress().trim());
        customerDTO.setDob(customerDTO.getDob().trim());
        customerDTO.setCitizenId(customerDTO.getCitizenId().trim());
        customerDTO.setPhoneNumber(customerDTO.getPhoneNumber().trim());
    }

    public static void validatorCustomerUpdateDTO(CustomerUpdateDTO customerUpdateDTO) {
        if (customerUpdateDTO.getFullName() != null) {
            notNullPhoneNumber(customerUpdateDTO.getFullName());
            customerUpdateDTO.setFullName(customerUpdateDTO.getFullName().trim());
        }

        if (customerUpdateDTO.getAddress() != null) {
            notNullAddress(customerUpdateDTO.getAddress());
            customerUpdateDTO.setAddress(customerUpdateDTO.getAddress().trim());
        }

        if (customerUpdateDTO.getDob() != null) {
            notNullDob(customerUpdateDTO.getDob());
            invalidDob(customerUpdateDTO.getCitizenId());
            customerUpdateDTO.setDob(customerUpdateDTO.getDob().trim());
        }

        if (customerUpdateDTO.getCitizenId() != null) {
            notNullCitizenId(customerUpdateDTO.getCitizenId());
            invalidCitizenId(customerUpdateDTO.getCitizenId());
            customerUpdateDTO.setDob(customerUpdateDTO.getCitizenId().trim());
        }

        if (customerUpdateDTO.getPhoneNumber() != null) {
            notNullPhoneNumber(customerUpdateDTO.getPhoneNumber());
            invalidPhoneNumber(customerUpdateDTO.getPhoneNumber());
            customerUpdateDTO.setPhoneNumber(customerUpdateDTO.getPhoneNumber().trim());
        }
    }
}
