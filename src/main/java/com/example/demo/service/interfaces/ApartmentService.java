package com.example.demo.service.interfaces;

import com.example.demo.entity.Apartment;
import com.example.demo.model.DTO.ApartmentDTO;
import com.example.demo.model.DTO.ApartmentUpdateDTO;

import java.util.List;

public interface ApartmentService {
    List<ApartmentDTO> getAll();

    Apartment getApartment(String id);

    ApartmentDTO getApartmentDTO(String id);

    ApartmentDTO create(ApartmentDTO apartmentToCreate);

    ApartmentDTO update(String id, ApartmentUpdateDTO apartmentUpdateDTO);

    ApartmentDTO delete(String id);

//    Apartment apartmentDTOToEntity(ApartmentDTO apartmentDTO);
//
//    ApartmentDTO apartmentEntityToDTO(Apartment apartment);
}
