package com.example.demo.service.interfaces;

import com.example.demo.entity.Apartment;
import com.example.demo.model.DTO.apartment.ApartmentDTO;
import com.example.demo.model.DTO.apartment.ApartmentUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    File exportCsv(boolean getTemplate);
}
