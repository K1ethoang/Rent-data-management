package com.example.demo.model.DTO.contract;

import com.example.demo.model.DTO.apartment.ApartmentDTO;
import com.example.demo.model.DTO.customer.CustomerDTO;
import com.example.demo.model.DTO.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractResponse {
    private String id;
    private String startDate;
    private String endDate;
    private String createDate;
    private Double total;
    private Double retailPrice;
    private CustomerDTO customer;
    private ApartmentDTO apartment;
    private UserDto user;
}
