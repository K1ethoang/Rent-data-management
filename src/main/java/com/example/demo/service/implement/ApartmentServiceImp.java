package com.example.demo.service.implement;

import com.example.demo.entity.Apartment;
import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.message.ApartmentMessage;
import com.example.demo.model.DTO.ApartmentDTO;
import com.example.demo.model.DTO.ApartmentUpdateDTO;
import com.example.demo.model.mapper.EntityToDto;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.interfaces.ApartmentService;
import com.example.demo.utils.MyUtils;
import com.example.demo.utils.validator.ApartmentValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class ApartmentServiceImp implements ApartmentService {
    private final ApartmentRepository apartmentRepository;
//    private final ModelMapper mapper;

    @Override
    public List<ApartmentDTO> getAll() throws NoContentException {
        List<Apartment> apartmentList = apartmentRepository.findAll();

        if (apartmentList.isEmpty()) throw new NoContentException(ApartmentMessage.EMPTY_LIST);

        List<ApartmentDTO> apartmentDTOList = new ArrayList<>() {
        };

        for (Apartment apartment : apartmentList) {
            apartmentDTOList.add(EntityToDto.apartmentToDto(apartment));
        }
        return apartmentDTOList;
    }

    @Override
    public Apartment getApartment(String id) throws NotFoundException {
        Optional<Apartment> optional = apartmentRepository.findById(id);

        if (optional.isEmpty()) throw new NotFoundException(ApartmentMessage.NOT_FOUND);

        return optional.get();
    }

    @Override
    public ApartmentDTO getApartmentDTO(String id) {
        Apartment apartment = getApartment(id);

        return EntityToDto.apartmentToDto(apartment);
    }

    @Override
    public ApartmentDTO create(ApartmentDTO apartmentDTO) {
        ApartmentValidator.validatorApartmentDTO(apartmentDTO);

        Apartment apartment = Apartment.builder()
                .address(apartmentDTO.getAddress().trim())
                .retailPrice(MyUtils.formatMoney(apartmentDTO.getRetailPrice().trim()))
                .numberOfRoom(Integer.parseInt(apartmentDTO.getNumberOfRoom().trim()))
                .build();

        apartmentRepository.save(apartment);

        return EntityToDto.apartmentToDto(apartment);
    }

    @Override
    public ApartmentDTO update(String id, ApartmentUpdateDTO apartmentUpdate) {
        ApartmentValidator.validatorApartmentUpdateDTO(apartmentUpdate);

        // Check apartment has in DB
        Apartment apartmentFromDB = getApartment(id);

        if (apartmentUpdate.getAddress() != null) {
            apartmentFromDB.setAddress(apartmentUpdate.getAddress().trim());
        }

        if (apartmentUpdate.getRetailPrice() != null) {
            apartmentFromDB.setRetailPrice(MyUtils.formatMoney(apartmentUpdate.getRetailPrice().trim()));
        }

        if (apartmentUpdate.getNumberOfRoom() != null) {
            apartmentFromDB.setNumberOfRoom(Integer.parseInt(apartmentUpdate.getNumberOfRoom().trim()));
        }

        ApartmentValidator.validatorApartmentDTO(EntityToDto.apartmentToDto(apartmentFromDB));

        apartmentRepository.save(apartmentFromDB);

        return EntityToDto.apartmentToDto(apartmentFromDB);
    }


    @Override
    public ApartmentDTO delete(String id) {
        // Check apartment has in DB
        Apartment apartmentToDelete = getApartment(id);

        apartmentRepository.delete(apartmentToDelete);

        return EntityToDto.apartmentToDto(apartmentToDelete);
    }

//    @Override
//    public Apartment apartmentDTOToEntity(ApartmentDTO apartmentDTO) {
//        return mapper.map(apartmentDTO, Apartment.class);
//    }
//
//
//    @Override
//    public ApartmentDTO apartmentEntityToDTO(Apartment apartment) {
//        return mapper.map(apartment, ApartmentDTO.class);
//    }
}
