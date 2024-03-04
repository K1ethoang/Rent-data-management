package com.example.demo.utils.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotNullException;
import com.example.demo.message.CustomerMessage;
import com.example.demo.model.DTO.customer.CustomerDTO;
import com.example.demo.model.DTO.customer.CustomerUpdateDTO;
import com.example.demo.utils.MyUtils;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomerValidator {

    private static void notNullFirstname(String firstName) throws NotNullException {
        if (firstName == null || firstName.trim().isEmpty())
            throw new NotNullException(CustomerMessage.NOT_NULL_FIRST_NAME);
    }

    private static void notNullLastname(String lastName) throws NotNullException {
        if (lastName == null || lastName.trim().isEmpty())
            throw new NotNullException(CustomerMessage.NOT_NULL_LAST_NAME);
    }

    private static void notNullAddress(String address) throws NotNullException {
        if (address == null || address.trim().isEmpty())
            throw new NotNullException(CustomerMessage.NOT_NULL_ADDRESS);
    }

    private static void notNullAge(String age) throws NotNullException {
        if (age == null || age.trim().isEmpty())
            throw new NotNullException(CustomerMessage.NOT_NULL_AGE);
    }

    private static void notNullStatus(String status) throws NotNullException {
        if (status == null || status.trim().isEmpty())
            throw new NotNullException(CustomerMessage.NOT_NULL_STATUS);
    }

    private static void invalidAge(String age) throws InValidException {
        if (!(MyUtils.stringToInteger(age.trim()) >= 18))
            throw new InValidException(CustomerMessage.INVALID_AGE);
    }

    public static void validatorCustomerDTO(CustomerDTO customerDTO) {
        notNullFirstname(customerDTO.getFirstName());
        notNullLastname(customerDTO.getLastName());
        notNullAddress(customerDTO.getAddress());
        notNullAge(customerDTO.getAge());
        invalidAge(customerDTO.getAge());

        customerDTO.setAddress(customerDTO.getAddress().trim());
        customerDTO.setFirstName(customerDTO.getFirstName().trim());
        customerDTO.setLastName(customerDTO.getLastName().trim());
        customerDTO.setAge(customerDTO.getAge().trim());
    }

    public static void validatorCustomerUpdateDTO(CustomerUpdateDTO customerUpdateDTO) {
        if (customerUpdateDTO.getFirstName() != null) {
            notNullFirstname(customerUpdateDTO.getFirstName());
            customerUpdateDTO.setFirstName(customerUpdateDTO.getFirstName().trim());
        }

        if (customerUpdateDTO.getLastName() != null) {
            notNullLastname(customerUpdateDTO.getLastName());
            customerUpdateDTO.setLastName(customerUpdateDTO.getLastName().trim());
        }

        if (customerUpdateDTO.getAddress() != null) {
            notNullAddress(customerUpdateDTO.getAddress());
            customerUpdateDTO.setAddress(customerUpdateDTO.getAddress().trim());
        }

        if (customerUpdateDTO.getAge() != null) {
            notNullAge(customerUpdateDTO.getAge());
            invalidAge(customerUpdateDTO.getAge());
            customerUpdateDTO.setAge(customerUpdateDTO.getAge().trim());
        }
    }
}
