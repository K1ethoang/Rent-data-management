package com.example.demo.model.validator;

import com.example.demo.exception.NotNullException;
import com.example.demo.exception.NotValidException;
import com.example.demo.message.CustomerMessage;
import com.example.demo.model.DTO.CustomerDTO;
import com.example.demo.utils.MyUtils;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustomerValidator {

    public static void notNullFirstname(String firstName) throws NotNullException {
        if (MyUtils.isNull(firstName)) return;
        if (MyUtils.isEmpty(firstName)) throw new NotNullException(CustomerMessage.NOT_NULL_FIRST_NAME);
    }

    public static void notNullLastname(String lastName) throws NotNullException {
        if (MyUtils.isNull(lastName)) return;
        if (MyUtils.isEmpty(lastName)) throw new NotNullException(CustomerMessage.NOT_NULL_LAST_NAME);
    }

    public static void notNullAddress(String address) throws NotNullException {
        if (MyUtils.isNull(address)) return;
        if (MyUtils.isEmpty(address)) throw new NotNullException(CustomerMessage.NOT_NULL_ADDRESS);
    }

    public static void notNullAge(String age) throws NotNullException {
        if (MyUtils.isNull(age)) return;
        if (MyUtils.isEmpty(age)) throw new NotNullException(CustomerMessage.NOT_NULL_AGE);
    }

    public static void notNullStatus(String status) throws NotNullException {
        if (MyUtils.isNull(status)) return;
        if (MyUtils.isEmpty(status)) throw new NotNullException(CustomerMessage.NOT_NULL_STATUS);
    }

    public static void invalidAge(String age) throws NotValidException {
        if (MyUtils.isNull(age) || MyUtils.isEmpty(age)) return;
        if (!(MyUtils.stringToNumeric(age) >= 18))
            throw new NotValidException(CustomerMessage.INVALID_AGE);
    }

    public static void validator(CustomerDTO customerDTO) {
        MyUtils.trimToSetAllField(customerDTO);

        notNullFirstname(customerDTO.getFirstName());
        notNullLastname(customerDTO.getLastName());
        notNullAddress(customerDTO.getAddress());
        notNullAge(customerDTO.getAge());
        notNullStatus(customerDTO.getStatus());

        invalidAge(customerDTO.getAge());
    }
}
