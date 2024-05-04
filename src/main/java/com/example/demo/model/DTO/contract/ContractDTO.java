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
    private String id;
    private String startDate;
    private String endDate;
    private String createDate;
    private String total;
    private String retailPrice;
    private String customerId;
    private String apartmentId;
    private String userId;
}