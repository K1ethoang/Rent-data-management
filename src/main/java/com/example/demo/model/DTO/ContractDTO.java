package com.example.demo.model.DTO;

import lombok.Data;

@Data
public class ContractDTO {
    private String id;
    private String startDate;
    private String endDate;
    private String customerId;
    private String apartmentId;
}