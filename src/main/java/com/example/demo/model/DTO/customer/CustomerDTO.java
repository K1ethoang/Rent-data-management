package com.example.demo.model.DTO.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private String id;
    private String address;
    private String fullName;
    private String citizenId;
    private String dob;
    private String phoneNumber;
}
