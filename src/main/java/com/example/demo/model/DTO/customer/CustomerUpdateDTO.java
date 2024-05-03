package com.example.demo.model.DTO.customer;

import lombok.Data;

@Data
public class CustomerUpdateDTO {
    private String address;
    private String fullName;
    private String citizenId;
    private String dob;
    private String phoneNumber;
}
