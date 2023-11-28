package com.example.demo.service.implement;

import com.example.demo.entity.Apartment;
import com.example.demo.exception.AppException;
import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.message.ApartmentMessage;
import com.example.demo.model.DTO.ApartmentDTO;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.interfaces.ApartmentService;
import com.example.demo.utils.MyUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
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
    public List<ApartmentDTO> getAll() throws NotFoundException {
        List<Apartment> apartments = apartmentRepository.findAll();

        if (apartments.isEmpty()) throw new NoContentException(ApartmentMessage.EMPTY_LIST);

        return mapper.map(apartments, new TypeToken<List<ApartmentDTO>>() {
        }.getType());
    }

    @Override
    public ApartmentDTO getOne(String id) throws NotFoundException {
        Apartment apartment = apartmentRepository.findById(id).orElse(null);

        if (apartment != null) {
            return apartmentEntityToDTO(apartment);
        } else throw new NotFoundException(ApartmentMessage.NOT_FOUND);
    }

    @Override
    public ApartmentDTO create(ApartmentDTO apartmentDTO) {
        return apartmentEntityToDTO(apartmentRepository.save(validateObject(apartmentDTOtoEntity(apartmentDTO))));
    }

    @Override
    public ApartmentDTO update(String id, Map<String, Object> payload) throws NotFoundException {
        Apartment apartmentFromDB = apartmentRepository.findById(id).orElse(null);


        if (apartmentFromDB != null) {
            payload.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Apartment.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, apartmentFromDB, value);
                }
            });

            return apartmentEntityToDTO(apartmentRepository.save(validateObject(apartmentFromDB)));
        } else throw new NotFoundException(ApartmentMessage.NOT_FOUND);
    }

    @Override
    public ApartmentDTO delete(String id) throws NotFoundException {
        Apartment apartmentFromDB = apartmentRepository.findById(id).orElse(null);

        if (apartmentFromDB != null) {
            apartmentRepository.delete(apartmentFromDB);
            return apartmentEntityToDTO(apartmentFromDB);
        } else throw new NotFoundException(ApartmentMessage.NOT_FOUND);
    }

    private Apartment validateObject(Apartment apartment) {
        int numberOfRoom = apartment.getNumberOfRoom();

        if (numberOfRoom < 0 || !MyUtils.isNumeric(String.valueOf(numberOfRoom))) {
            throw new AppException(HttpStatus.BAD_REQUEST, ApartmentMessage.INVALID_DATA);
        }

        double retailPrice = MyUtils.stringToNumeric(apartment.getRetailPrice());

        if (retailPrice < 0) {
            throw new AppException(HttpStatus.BAD_REQUEST, ApartmentMessage.INVALID_DATA);
        }

        apartment.setRetailPrice(MyUtils.formatMoney(retailPrice));

        return apartment;
    }

    private Apartment apartmentDTOtoEntity(ApartmentDTO apartmentDTO) {
        return mapper.map(apartmentDTO, Apartment.class);
    }

    private ApartmentDTO apartmentEntityToDTO(Apartment apartment) {
        return mapper.map(apartment, ApartmentDTO.class);
    }
}
