package com.example.demo.service.implement;

import com.example.demo.entity.Apartment;
import com.example.demo.exception.InValidException;
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
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        List<ApartmentDTO> apartmentDTOList = new ArrayList<>();

        apartmentRepository.findAll().forEach(apartment -> apartmentDTOList.add(EntityToDto.apartmentToDto(apartment)));

        if (apartmentDTOList.isEmpty()) throw new NoContentException(ApartmentMessage.EMPTY_LIST);

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
    public ApartmentDTO create(ApartmentDTO apartmentToCreate) {
        ApartmentValidator.validatorApartmentDTO(apartmentToCreate);

        checkDuplicated(apartmentToCreate);

        Apartment apartment = Apartment.builder()
                .address(apartmentToCreate.getAddress())
                .retailPrice(apartmentToCreate.getRetailPrice())
                .numberOfRoom(Integer.parseInt(apartmentToCreate.getNumberOfRoom()))
                .build();

        Apartment storedApartmentDetails = apartmentRepository.save(apartment);

        return EntityToDto.apartmentToDto(storedApartmentDetails);
    }

    @Override
    public ApartmentDTO update(String id, ApartmentUpdateDTO apartmentUpdate) {
        ApartmentValidator.validatorApartmentUpdateDTO(apartmentUpdate);

        Apartment apartmentFromDB = getApartment(id);

        if (apartmentUpdate.getAddress() != null) {
            apartmentFromDB.setAddress(apartmentUpdate.getAddress());
        }
        if (apartmentUpdate.getRetailPrice() != null) {
            apartmentFromDB.setRetailPrice(apartmentUpdate.getRetailPrice());
        }
        if (apartmentUpdate.getNumberOfRoom() != null) {
            apartmentFromDB.setNumberOfRoom(Integer.parseInt(apartmentUpdate.getNumberOfRoom()));
        }

        checkDuplicated(EntityToDto.apartmentToDto(apartmentFromDB));

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

    // Kiểm tra trùng dữ liệu
    public void checkDuplicated(ApartmentDTO apartmentToCheck) {
        List<Apartment> apartmentList = apartmentRepository.findAll();

        boolean isDuplicated = false;

        for (Apartment apartment : apartmentList) {
            ApartmentDTO apartmentDTO = EntityToDto.apartmentToDto(apartment);

            log.info(apartmentDTO);

            if (!apartmentDTO.getAddress().equalsIgnoreCase(apartmentToCheck.getAddress())) continue;
            if (!apartmentDTO.getRetailPrice().equals(apartmentToCheck.getRetailPrice())) continue;
            if (!apartmentDTO.getNumberOfRoom().equals(apartmentToCheck.getNumberOfRoom())) continue;

            isDuplicated = true;
            break;
        }

        if (isDuplicated) throw new InValidException(ApartmentMessage.APARTMENT_EXIST);
    }

//    @Override
//    public Apartment apartmentDTOToEntity(ApartmentDTO apartmentDTO) {
//        return mapper.map(apartmentDTO, Apartment.class);
//    }
//
//    @Override
//    public ApartmentDTO apartmentEntityToDTO(Apartment apartment) {
//        return mapper.map(apartment, ApartmentDTO.class);
//    }
}
