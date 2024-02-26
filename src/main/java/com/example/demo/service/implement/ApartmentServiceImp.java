package com.example.demo.service.implement;

import com.example.demo.entity.Apartment;
import com.example.demo.exception.DuplicatedException;
import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.helpers.CsvHelper;
import com.example.demo.message.ApartmentMessage;
import com.example.demo.message.FileMessage;
import com.example.demo.model.DTO.ApartmentDTO;
import com.example.demo.model.DTO.ApartmentUpdateDTO;
import com.example.demo.model.mapper.EntityToDto;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.service.interfaces.ApartmentService;
import com.example.demo.utils.validator.ApartmentValidator;
import com.example.demo.utils.validator.FileValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

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

        Apartment tempApartment = Apartment.builder()
                .address(apartmentFromDB.getAddress())
                .retailPrice(apartmentFromDB.getRetailPrice())
                .numberOfRoom(apartmentFromDB.getNumberOfRoom())
                .build();

        if (apartmentUpdate.getAddress() != null) {
//            apartmentFromDB.setAddress(apartmentUpdate.getAddress());
            tempApartment.setAddress(apartmentUpdate.getAddress());
        }
        if (apartmentUpdate.getRetailPrice() != null) {
//            apartmentFromDB.setRetailPrice(apartmentUpdate.getRetailPrice());
            tempApartment.setRetailPrice(apartmentUpdate.getRetailPrice());
        }
        if (apartmentUpdate.getNumberOfRoom() != null) {
//            apartmentFromDB.setNumberOfRoom(Integer.parseInt(apartmentUpdate.getNumberOfRoom()));
            tempApartment.setNumberOfRoom(Integer.parseInt(apartmentUpdate.getNumberOfRoom()));
        }

//        checkDuplicated(EntityToDto.apartmentToDto(apartmentFromDB));
        checkDuplicated(EntityToDto.apartmentToDto(tempApartment));

        apartmentFromDB.setRetailPrice(tempApartment.getRetailPrice());
        apartmentFromDB.setAddress(tempApartment.getAddress());
        apartmentFromDB.setNumberOfRoom(tempApartment.getNumberOfRoom());

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

    @Override
    public List<Object> loadApartments(MultipartFile[] files) {
        List<Object> response = new ArrayList<>();

        for (MultipartFile file : files) {
            response.add(loadApartment(file));
        }

        return response;
    }

    @Override
    public Map<String, Object> loadApartment(MultipartFile file) {
        FileValidator.validatorMultipartFile(file);

        Map<String, Object> response = new HashMap<>();
        response.put("File", file.getOriginalFilename());

        List<ApartmentDTO> apartmentList = null;

        if (CsvHelper.hasCsvFormat(file)) apartmentList = CsvHelper.csvToApartments(file);

        int numberOfApartmentAdded = 0;

        String failedRows = "";

        for (int i = 0; i < apartmentList.size(); i++) {
            try {
                ApartmentValidator.validatorApartmentDTO(apartmentList.get(i));
            } catch (Exception e) {
                failedRows = failedRows.concat((i + 1) + " , ");
                continue;
            }

            try {
                checkDuplicated(apartmentList.get(i));
            } catch (DuplicatedException e) {
                failedRows = failedRows.concat((i + 1) + " , ");
                continue;
            }

            Apartment apartmentToAdd = Apartment.builder()
                    .address(apartmentList.get(i).getAddress())
                    .numberOfRoom(Integer.parseInt(apartmentList.get(i).getNumberOfRoom()))
                    .retailPrice(apartmentList.get(i).getRetailPrice())
                    .build();


            apartmentRepository.save(apartmentToAdd);
            numberOfApartmentAdded++;
        }

        response.put(FileMessage.NUMBER_SUCCESS_ROW, numberOfApartmentAdded);

        if (!failedRows.isEmpty())
            failedRows = failedRows.substring(0, failedRows.length() - 3);
        response.put(FileMessage.FAILED_ROWS, failedRows);

        return response;
    }

    // Kiểm tra trùng dữ liệu
    public void checkDuplicated(ApartmentDTO apartmentToCheck) throws DuplicatedException {
        List<Apartment> apartmentList = apartmentRepository.findAll();

        boolean isDuplicated = false;

        for (Apartment apartment : apartmentList) {
            ApartmentDTO apartmentDTO = EntityToDto.apartmentToDto(apartment);

            if (!apartmentDTO.getAddress().equalsIgnoreCase(apartmentToCheck.getAddress()))
                continue;
            if (!apartmentDTO.getRetailPrice().equals(apartmentToCheck.getRetailPrice())) continue;
            if (!apartmentDTO.getNumberOfRoom().equals(apartmentToCheck.getNumberOfRoom()))
                continue;

            isDuplicated = true;
            break;
        }

        if (isDuplicated) throw new DuplicatedException(ApartmentMessage.APARTMENT_EXIST);
    }

    @Override
    public File exportCsv(boolean getTemplate) {
        try {
            List<ApartmentDTO> apartmentList = new ArrayList<>();
            apartmentRepository.findAll().forEach(apartment -> apartmentList.add(EntityToDto.apartmentToDto(apartment)));

            return CsvHelper.exportApartments(apartmentList, getTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
