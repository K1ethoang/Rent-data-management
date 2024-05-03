package com.example.demo.model.DTO.apartment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentDTO {
    private String id;
    private String address;
    private String retailPrice;
    private String numberOfRoom;
}
