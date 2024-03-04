package com.example.demo.model.DTO.apartment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApartmentDTO {
    private String address;
    private String retailPrice;
    private String numberOfRoom;
}
