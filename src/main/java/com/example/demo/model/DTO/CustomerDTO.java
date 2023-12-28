package com.example.demo.model.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private String firstName;
    private String lastName;
    private String address;
    private String age;
}
