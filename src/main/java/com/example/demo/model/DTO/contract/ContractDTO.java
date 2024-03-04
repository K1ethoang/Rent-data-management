package com.example.demo.model.DTO.contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContractDTO {
    private String startDate;
    private String endDate;
    private String customerId;
    private String apartmentId;
}