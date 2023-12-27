package com.example.demo.model.validator;

import com.example.demo.exception.NotNullException;
import com.example.demo.exception.NotValidException;
import com.example.demo.message.ApartmentMessage;
import com.example.demo.model.DTO.ApartmentDTO;
import com.example.demo.utils.MyUtils;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApartmentValidator {

    public static void notNullAddress(String address) throws NotNullException {
        if (MyUtils.isNull(address)) return;
        if (MyUtils.isEmpty(address)) throw new NotNullException(ApartmentMessage.NOT_NULL_ADDRESS);
    }

    public static void notNullRetailPrice(String retailPrice) throws NotNullException {
        if (MyUtils.isNull(retailPrice)) return;
        if (MyUtils.isEmpty(retailPrice)) throw new NotNullException(ApartmentMessage.NOT_NULL_RETAIL_PRICE);
    }

    public static void notNullNumberOfRoom(String numberOfRoom) throws NotNullException {
        if (MyUtils.isNull(numberOfRoom)) return;
        if (MyUtils.isEmpty(numberOfRoom)) throw new NotNullException(ApartmentMessage.NOT_NULL_NUMBER_OF_ROOM);
    }

    public static void invalidRetailPrice(String retailPrice) throws NotValidException {
        if (MyUtils.isNull(retailPrice) || MyUtils.isEmpty(retailPrice)) return;
        if (MyUtils.stringToNumeric(retailPrice) < 0)
            throw new NotValidException(ApartmentMessage.INVALID_RETAIL_PRICE);
    }

    public static void invalidNumberOfRoom(String numberOfRoom) throws NotValidException {
        if (MyUtils.isNull(numberOfRoom) || MyUtils.isEmpty(numberOfRoom)) return;
        if (MyUtils.stringToNumeric(numberOfRoom) < 0)
            throw new NotValidException(ApartmentMessage.INVALID_NUMBER_OF_ROOM);
    }

    public static void validator(ApartmentDTO apartmentDTO) {
        MyUtils.trimToSetAllField(apartmentDTO);

        notNullAddress(apartmentDTO.getAddress());
        notNullRetailPrice(apartmentDTO.getRetailPrice());
        notNullNumberOfRoom(apartmentDTO.getNumberOfRoom());

        invalidRetailPrice(apartmentDTO.getRetailPrice());
        invalidNumberOfRoom(apartmentDTO.getNumberOfRoom());

        format(apartmentDTO);
    }

    public static void format(ApartmentDTO apartmentDTO) {
        if (!MyUtils.isNull(apartmentDTO.getRetailPrice()))
            apartmentDTO.setRetailPrice(MyUtils.formatMoney(apartmentDTO.getRetailPrice()));
    }
}
