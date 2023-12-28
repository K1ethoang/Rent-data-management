package com.example.demo.model.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApartmentUpdateDTO {
    private String address;
    private String retailPrice;
    private String numberOfRoom;
}
