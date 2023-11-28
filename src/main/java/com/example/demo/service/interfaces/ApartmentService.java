package com.example.demo.service.interfaces;

import com.example.demo.model.DTO.ApartmentDTO;

import java.util.List;
import java.util.Map;

public interface ApartmentService {
    List<ApartmentDTO> getAll();

    ApartmentDTO getOne(String id);

    ApartmentDTO create(ApartmentDTO apartmentDTO);

    ApartmentDTO update(String id, Map<String, Object> payload);

    ApartmentDTO delete(String id);
}
