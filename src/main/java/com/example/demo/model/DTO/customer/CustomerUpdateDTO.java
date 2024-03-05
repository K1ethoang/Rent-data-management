package com.example.demo.model.DTO.customer;

import lombok.Data;

@Data
public class CustomerUpdateDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String age;
}
