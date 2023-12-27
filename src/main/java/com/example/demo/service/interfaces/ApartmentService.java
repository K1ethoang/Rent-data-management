package com.example.demo.service.interfaces;

import com.example.demo.model.DTO.ApartmentDTO;
import com.example.demo.model.entity.Apartment;

import java.util.List;
import java.util.Map;

public interface ApartmentService {
    List<ApartmentDTO> getAll();

    ApartmentDTO getOneById(String id);

    ApartmentDTO create(ApartmentDTO apartmentDTO);

    ApartmentDTO update(String id, Map<String, Object> payload);

    ApartmentDTO delete(String id);

    Apartment apartmentDTOToEntity(ApartmentDTO apartmentDTO);

    ApartmentDTO apartmentEntityToDTO(Apartment apartment);
}
