package com.example.demo.service.implement;

import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.helpers.Helper;
import com.example.demo.message.ApartmentMessage;
import com.example.demo.model.DTO.ApartmentDTO;
import com.example.demo.model.entity.Apartment;
import com.example.demo.model.validator.ApartmentValidator;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.interfaces.ApartmentService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Log4j2
public class ApartmentServiceImp implements ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final ModelMapper mapper;

    @Override
    public List<ApartmentDTO> getAll() throws NoContentException {
        List<Apartment> apartments = apartmentRepository.findAll();

        if (apartments.isEmpty()) throw new NoContentException(ApartmentMessage.EMPTY_LIST);

        return mapper.map(apartments, new TypeToken<List<ApartmentDTO>>() {
        }.getType());
    }

    @Override
    public ApartmentDTO getOneById(String id) throws NotFoundException {
        Apartment apartmentFromDB = apartmentRepository.findById(id).orElse(null);

        if (apartmentFromDB != null) {
            return apartmentEntityToDTO(apartmentFromDB);
        } else throw new NotFoundException(ApartmentMessage.NOT_FOUND);
    }

    @Override
    public ApartmentDTO create(ApartmentDTO apartmentDTO) {
        Helper.setAllFieldNullToEmpty(apartmentDTO);

        ApartmentValidator.validator(apartmentDTO);
        return apartmentEntityToDTO(apartmentRepository.save(apartmentDTOToEntity(apartmentDTO)));
    }

    @Override
    public ApartmentDTO update(String id, Map<String, Object> payload) throws NotFoundException {
        // Find
        Apartment apartmentFromDB = apartmentRepository.findById(id).orElse(null);
        if (apartmentFromDB == null) throw new NotFoundException(ApartmentMessage.NOT_FOUND);

        // Map payload -> DTO to validate
        ApartmentDTO apartmentFromPayload = mapper.map(payload, ApartmentDTO.class);
        // Validate
        ApartmentValidator.validator(apartmentFromPayload);

        ApartmentDTO apartmentToUpdate = apartmentEntityToDTO(apartmentFromDB);

        payload.forEach((key, value) -> {
            if (value != null) {
                Field field = ReflectionUtils.findField(ApartmentDTO.class, key);

                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, apartmentToUpdate, ReflectionUtils.getField(field, apartmentFromPayload));
                }
            }

        });

        return apartmentEntityToDTO(apartmentRepository.save(apartmentDTOToEntity(apartmentToUpdate)));
    }


    @Override
    public ApartmentDTO delete(String id) throws NotFoundException {
        Apartment apartmentFromDB = apartmentRepository.findById(id).orElse(null);

        if (apartmentFromDB != null) {
            apartmentRepository.delete(apartmentFromDB);
            return apartmentEntityToDTO(apartmentFromDB);
        } else throw new NotFoundException(ApartmentMessage.NOT_FOUND);
    }

    @Override
    public Apartment apartmentDTOToEntity(ApartmentDTO apartmentDTO) {
        return mapper.map(apartmentDTO, Apartment.class);
    }


    @Override
    public ApartmentDTO apartmentEntityToDTO(Apartment apartment) {
        return mapper.map(apartment, ApartmentDTO.class);
    }
}
