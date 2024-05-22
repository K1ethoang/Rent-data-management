package com.example.demo.message;

public class CustomerMessage {
    public static final String NOT_FOUND = "Customer not found";
    public static final String EMPTY_LIST = "Customer list are empty";
    public static final String CUSTOMER_EXIST = "Customer already exists";
    public static final String DUPLICATED_PHONE_NUMBER = "Has customer with this phone number";
    public static final String DUPLICATED_CITIZEN_ID = "Has customer with this citizen id";

    // Not null
    public static final String NOT_NULL_FULL_NAME = "Full name is not null";
    public static final String NOT_NULL_CITIZEN_ID = "Citizen Id is not null";
    public static final String NOT_NULL_DOB = "Dob is not null";
    public static final String NOT_NULL_PHONE_NUMBER = "Phone number is not null";
    public static final String NOT_NULL_ADDRESS = "Address is not null";

    // Invalid
    public static final String INVALID_DOB = "Dob must be less than now date";
    public static final String CUSTOMER_IN_CONTRACT = "Customer is in a contract";
    public static final String INVALID_PHONE_NUMBER = "Phone number must is number and right " +
            "format";
    public static final String INVALID_CITIZEN_ID = "Length of citizen id must is number and " +
            "length is 12";

}
