package com.example.demo.service.interfaces;

import com.example.demo.entity.Apartment;
import com.example.demo.model.DTO.ApartmentDTO;
import com.example.demo.model.DTO.ApartmentUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ApartmentService {
    List<ApartmentDTO> getAll();

    Apartment getApartment(String id);

    ApartmentDTO getApartmentDTO(String id);

    ApartmentDTO create(ApartmentDTO apartmentToCreate);

    ApartmentDTO update(String id, ApartmentUpdateDTO apartmentUpdateDTO);

    ApartmentDTO delete(String id);

    List<Object> loadApartments(MultipartFile[] files);

    Map<String, Object> loadApartment(MultipartFile file);
//    Apartment apartmentDTOToEntity(ApartmentDTO apartmentDTO);
//
//    ApartmentDTO apartmentEntityToDTO(Apartment apartment);
}
