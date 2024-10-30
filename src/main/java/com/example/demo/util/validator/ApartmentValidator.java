package com.example.demo.util.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotNullException;
import com.example.demo.message.ApartmentMessage;
import com.example.demo.model.DTO.apartment.ApartmentDTO;
import com.example.demo.model.DTO.apartment.ApartmentUpdateDTO;
import com.example.demo.util.MyUtils;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApartmentValidator {
    public static final int MAX_ROOM = 20;
    public static final double MAX_PRICE = 100000000000.0;

    public static void notNullAddress(String address) throws NotNullException {
        if (address == null || address.trim().isEmpty())
            throw new NotNullException(ApartmentMessage.NOT_NULL_ADDRESS);
    }

    public static void notNullRetailPrice(String retailPrice) throws NotNullException {
        if (retailPrice == null || retailPrice.trim().isEmpty())
            throw new NotNullException(ApartmentMessage.NOT_NULL_RETAIL_PRICE);
    }

    public static void notNullNumberOfRoom(String numberOfRoom) throws NotNullException {
        if (numberOfRoom == null || numberOfRoom.trim().isEmpty())
            throw new NotNullException(ApartmentMessage.NOT_NULL_NUMBER_OF_ROOM);
    }

    public static void invalidRetailPrice(String retailPrice) throws InValidException {
        if (MyUtils.stringToDouble(retailPrice) <= 0 || MyUtils.stringToDouble(retailPrice) > MAX_PRICE)
            throw new InValidException(ApartmentMessage.INVALID_RETAIL_PRICE);
    }

    public static void invalidNumberOfRoom(String numberOfRoom) throws InValidException {
        if (MyUtils.stringToInteger(numberOfRoom) <= 0 || MyUtils.stringToInteger(numberOfRoom) > MAX_ROOM)
            throw new InValidException(ApartmentMessage.INVALID_NUMBER_OF_ROOM);
    }

    public static void validatorApartmentDTO(ApartmentDTO apartmentDTO) {
        notNullAddress(apartmentDTO.getAddress());
        notNullRetailPrice(apartmentDTO.getRetailPrice());
        notNullNumberOfRoom(apartmentDTO.getNumberOfRoom());

        invalidRetailPrice(apartmentDTO.getRetailPrice());
        invalidNumberOfRoom(apartmentDTO.getNumberOfRoom());

        apartmentDTO.setAddress(apartmentDTO.getAddress().trim());
        apartmentDTO.setRetailPrice(apartmentDTO.getRetailPrice().trim());
        apartmentDTO.setRetailPrice(String.valueOf(MyUtils.stringToDouble(apartmentDTO.getRetailPrice().trim())));
        apartmentDTO.setNumberOfRoom(apartmentDTO.getNumberOfRoom().trim());
    }

    public static void validatorApartmentUpdateDTO(ApartmentUpdateDTO apartmentUpdateDTO) {
        if (apartmentUpdateDTO.getAddress() != null) {
            notNullAddress(apartmentUpdateDTO.getAddress());
            apartmentUpdateDTO.setAddress(apartmentUpdateDTO.getAddress().trim());
        }

        if (apartmentUpdateDTO.getRetailPrice() != null) {
            notNullRetailPrice(apartmentUpdateDTO.getRetailPrice());
            invalidRetailPrice(apartmentUpdateDTO.getRetailPrice());
            apartmentUpdateDTO.setRetailPrice(String.valueOf(MyUtils.stringToDouble(apartmentUpdateDTO.getRetailPrice().trim())));
            apartmentUpdateDTO.setRetailPrice(apartmentUpdateDTO.getRetailPrice().trim());
        }

        if (apartmentUpdateDTO.getNumberOfRoom() != null) {
            notNullAddress(apartmentUpdateDTO.getNumberOfRoom());
            invalidNumberOfRoom(apartmentUpdateDTO.getNumberOfRoom());
            apartmentUpdateDTO.setNumberOfRoom(apartmentUpdateDTO.getNumberOfRoom().trim());
        }
    }
}
