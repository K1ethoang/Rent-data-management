package com.example.demo.utils.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotNullException;
import com.example.demo.message.ApartmentMessage;
import com.example.demo.model.DTO.ApartmentDTO;
import com.example.demo.model.DTO.ApartmentUpdateDTO;
import com.example.demo.utils.MyUtils;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApartmentValidator {

    public static void notNullAddress(String address) throws NotNullException {
        if (address == null || address.trim().isEmpty()) throw new NotNullException(ApartmentMessage.NOT_NULL_ADDRESS);
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
        if (MyUtils.stringToDouble(retailPrice) < 0)
            throw new InValidException(ApartmentMessage.INVALID_RETAIL_PRICE);
    }

    public static void invalidNumberOfRoom(String numberOfRoom) throws InValidException {
        if (MyUtils.stringToInteger(numberOfRoom) <= 0)
            throw new InValidException(ApartmentMessage.INVALID_NUMBER_OF_ROOM);
    }

    public static void validatorApartmentDTO(ApartmentDTO apartmentDTO) {
        notNullAddress(apartmentDTO.getAddress());
        notNullRetailPrice(apartmentDTO.getRetailPrice());
        invalidRetailPrice(apartmentDTO.getRetailPrice());
        notNullNumberOfRoom(apartmentDTO.getNumberOfRoom());
        invalidNumberOfRoom(apartmentDTO.getNumberOfRoom());

        apartmentDTO.setAddress(apartmentDTO.getAddress().trim());
        apartmentDTO.setRetailPrice(apartmentDTO.getRetailPrice().trim());
        apartmentDTO.setRetailPrice(MyUtils.formatMoney(apartmentDTO.getRetailPrice()));
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
            apartmentUpdateDTO.setRetailPrice(apartmentUpdateDTO.getRetailPrice().trim());
        }

        if (apartmentUpdateDTO.getNumberOfRoom() != null) {
            notNullAddress(apartmentUpdateDTO.getNumberOfRoom());
            invalidNumberOfRoom(apartmentUpdateDTO.getNumberOfRoom());
            apartmentUpdateDTO.setNumberOfRoom(apartmentUpdateDTO.getNumberOfRoom().trim());
        }
    }
}
