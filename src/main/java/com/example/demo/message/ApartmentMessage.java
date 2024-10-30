package com.example.demo.message;

import com.example.demo.util.validator.ApartmentValidator;

public class ApartmentMessage {
    public static final String NOT_FOUND = "Apartment not found";
    public static final String EMPTY_LIST = "Apartment list are empty";
    public static final String APARTMENT_EXIST = "Apartment already exists";
    public static final String NOT_NULL_ADDRESS = "Address is not null";
    public static final String APARTMENT_IN_CONTRACT = "Apartment is in a contract";
    public static final String NOT_NULL_RETAIL_PRICE = "Retail Price is not null";
    public static final String INVALID_RETAIL_PRICE = "Retail Price must be " +
            "number and in range [1, "+ ApartmentValidator.MAX_PRICE + "]";
    public static final String NOT_NULL_NUMBER_OF_ROOM = "Number of room is not null";
    public static final String INVALID_NUMBER_OF_ROOM = "Number of room must " +
            "be number and in range [1, " + ApartmentValidator.MAX_ROOM + "]";
}
