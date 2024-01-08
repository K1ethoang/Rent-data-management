package com.example.demo.message;

public class ContractMessage {
    public static final String NOT_FOUND = "Contract not found";
    public static final String EMPTY_LIST = "Contract list are empty";
    public static final String CONTRACT_EXIST = "Contract already exists";

    public static final String NOT_NULL_START_DATE = "Start Date is not null";
    public static final String NOT_NULL_END_DATE = "End Date is not null";

    public static final String NOT_NULL_CUSTOMER_ID = "Customer Id is not null";

    public static final String NOT_NULL_APARTMENT_ID = "Apartment Id is not null";

    public static final String INCORRECT_FORMAT_DATE = "Date must be in \"yyyy-mm-dd\" format";
    public static final String INVALID_START_DATE = "Start Date is invalid or not be in " +
            "\"yyyy-mm-dd\" format";
    public static final String INVALID_END_DATE = "End Date is invalid or not be in " +
            "\"yyyy-mm-dd\" format ";
    public static final String START_DATE_LESS_THAN_END_DATE = "Start Date must be less than End Date";

    public static final String NOT_EXIST_CUSTOMER_ID = "Customer id is not exist";
    public static final String NOT_EXIST_APARTMENT_ID = "Apartment id is not exist";
}
