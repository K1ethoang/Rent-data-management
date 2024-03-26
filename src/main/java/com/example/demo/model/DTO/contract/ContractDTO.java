package com.example.demo.model.DTO.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractDTO {
    private String startDate;
    private String endDate;
    private String customerId;
    private String apartmentId;
}