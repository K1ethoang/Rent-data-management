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
        if (MyUtils.isNullOrEmpty(address)) throw new NotNullException(ApartmentMessage.NULL_ADDRESS);
    }

    public static void notNullRetailPrice(String retailPrice) throws NotNullException {
        if (MyUtils.isNullOrEmpty(retailPrice)) throw new NotNullException(ApartmentMessage.NULL_RETAIL_PRICE);
    }

    public static void notNullNumberOfRoom(String numberOfRoom) throws NotNullException {
        if (MyUtils.isNullOrEmpty(numberOfRoom)) throw new NotNullException(ApartmentMessage.NULL_NUMBER_OF_ROOM);
    }

    public static void invalidRetailPrice(String retailPrice) throws NotValidException {
        if (MyUtils.isNullOrEmpty(retailPrice)) return;
        if (MyUtils.stringToNumeric(retailPrice) < 0)
            throw new NotValidException(ApartmentMessage.INVALID_RETAIL_PRICE);
    }

    public static void invalidNumberOfRoom(String numberOfRoom) throws NotValidException {
        if (MyUtils.isNullOrEmpty(numberOfRoom)) return;
        if (MyUtils.stringToNumeric(numberOfRoom) < 0)
            throw new NotValidException(ApartmentMessage.INVALID_NUMBER_OF_ROOM);
    }

    public static void validator(ApartmentDTO apartmentDTO, boolean hasCheckNull) {
        log.info("START: validator");
        if (hasCheckNull) {
            log.info("CHECK NULL");
            notNullAddress(apartmentDTO.getAddress().trim());
            notNullRetailPrice(apartmentDTO.getRetailPrice().trim());
            notNullNumberOfRoom(apartmentDTO.getNumberOfRoom().trim());
        }

        log.info("TRIM DATA");
        trim(apartmentDTO);

        log.info("VALIDATE");
        invalidRetailPrice(apartmentDTO.getRetailPrice());
        invalidNumberOfRoom(apartmentDTO.getNumberOfRoom());

        log.info("END: validator");
    }

    public static void trim(ApartmentDTO apartmentDTO) {
        // trim
        if (!MyUtils.isNullOrEmpty(apartmentDTO.getAddress()))
            apartmentDTO.setAddress(apartmentDTO.getAddress().trim());

        if (!MyUtils.isNullOrEmpty(apartmentDTO.getRetailPrice()))
            apartmentDTO.setRetailPrice(apartmentDTO.getRetailPrice().trim());

        if (!MyUtils.isNullOrEmpty(apartmentDTO.getNumberOfRoom()))
            apartmentDTO.setNumberOfRoom(apartmentDTO.getNumberOfRoom().trim());
    }

    public static void format(ApartmentDTO apartmentDTO) {
        if (!MyUtils.isNullOrEmpty(apartmentDTO.getRetailPrice()))
            apartmentDTO.setRetailPrice(MyUtils.formatMoney(apartmentDTO.getRetailPrice()));
    }
}
