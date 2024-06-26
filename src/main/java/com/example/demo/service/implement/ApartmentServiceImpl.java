package com.example.demo.service.implement;

import com.example.demo.entity.Apartment;
import com.example.demo.entity.Contract;
import com.example.demo.exception.DuplicatedException;
import com.example.demo.exception.InValidException;
import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.helper.CsvHelper;
import com.example.demo.message.ApartmentMessage;
import com.example.demo.message.FileMessage;
import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.apartment.ApartmentDTO;
import com.example.demo.model.DTO.apartment.ApartmentUpdateDTO;
import com.example.demo.model.DTO.paging.APIPageableDTO;
import com.example.demo.model.mapper.EntityToDto;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.ContractRepository;
import com.example.demo.service.interfaces.ApartmentService;
import com.example.demo.util.validator.ApartmentValidator;
import com.example.demo.util.validator.FileValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
@AllArgsConstructor
@Log4j2
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final ContractRepository contractRepository;

    @Override
    public Map<String, Object> getAll(Pageable pageable) throws NoContentException {
        Map<String, Object> result = new HashMap<>();

        Page<Apartment> pageEntity =
                apartmentRepository.findAll(pageable);

        Page<ApartmentDTO> pageDto =
                pageEntity.map(EntityToDto::apartmentToDto);

        APIPageableDTO apiPageableDTO = new APIPageableDTO(pageDto);

        result.put("page", apiPageableDTO);
        result.put("apartments", pageDto.getContent());

        return result;
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
                .retailPrice(Double.parseDouble(apartmentToCreate.getRetailPrice()))
                .numberOfRoom(Integer.parseInt(apartmentToCreate.getNumberOfRoom()))
                .build();

        Apartment storedApartmentDetails = apartmentRepository.save(apartment);

        return EntityToDto.apartmentToDto(storedApartmentDetails);
    }

    @Override
    public ApartmentDTO update(String id, ApartmentUpdateDTO apartmentUpdate) {
        ApartmentValidator.validatorApartmentUpdateDTO(apartmentUpdate);

        Apartment apartmentFromDB = getApartment(id);

        ApartmentDTO tempApartment = EntityToDto.apartmentToDto(apartmentFromDB);

        if (apartmentUpdate.getAddress() != null) {
            tempApartment.setAddress(apartmentUpdate.getAddress());
        }
        if (apartmentUpdate.getRetailPrice() != null) {
            tempApartment.setRetailPrice(apartmentUpdate.getRetailPrice());
        }
        if (apartmentUpdate.getNumberOfRoom() != null) {
            tempApartment.setNumberOfRoom(apartmentUpdate.getNumberOfRoom());
        }

        checkDuplicated(tempApartment);

        apartmentFromDB.setRetailPrice(Double.parseDouble(tempApartment.getRetailPrice()));
        apartmentFromDB.setAddress(tempApartment.getAddress());
        apartmentFromDB.setNumberOfRoom(Integer.parseInt(tempApartment.getNumberOfRoom()));

        apartmentRepository.save(apartmentFromDB);

        return EntityToDto.apartmentToDto(apartmentFromDB);
    }

    @Override
    public ApartmentDTO delete(String id) throws InValidException {
        // Check apartment has in DB
        Apartment apartmentToDelete = getApartment(id);

        if (checkApartmentIsUsed(id)) {
            throw new InValidException(ApartmentMessage.APARTMENT_IN_CONTRACT);
        }

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
                    .retailPrice(Double.parseDouble(apartmentList.get(i).getRetailPrice()))
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

            if (apartmentDTO.getId().equals(apartmentToCheck.getId()))
                continue;

            if (!apartmentDTO.getAddress().equalsIgnoreCase(apartmentToCheck.getAddress()))
                continue;
            if (!apartmentDTO.getRetailPrice().equals(apartmentToCheck.getRetailPrice()))
                continue;
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
            List<ApartmentDTO> apartmentDTOS = new ArrayList<>();
            apartmentRepository.findAll().forEach(apartment -> apartmentDTOS.add(EntityToDto.apartmentToDto(apartment)));
            return CsvHelper.exportApartments(apartmentDTOS,
                    getTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> search(String query, Pageable pageable) throws InValidException {
        if (query == null || query.trim().isEmpty())
            throw new InValidException(GlobalMessage.NOT_NULL_QUERY);

        Map<String, Object> result = new HashMap<>();

        Page<Apartment> pageEntity =
                apartmentRepository.search(query, pageable);
        Page<ApartmentDTO> pageDTOS = pageEntity.map(EntityToDto::apartmentToDto);

        APIPageableDTO apiPageableDTO = new APIPageableDTO(pageDTOS);

        result.put("page", apiPageableDTO);
        result.put("apartments", pageDTOS.getContent());

        return result;
    }

    @Override
    public Map<String, Object> statistic() {
        Map<String, Object> res = new HashMap<>();

        long count = apartmentRepository.count();

        if (count == 0) return null;

        res.put("count", count);

        return res;
    }

    boolean checkApartmentIsUsed(String apartmentId) {
        List<Contract> contractList = contractRepository.getContractsByApartment_Id(apartmentId);

        if (contractList.isEmpty()) return false;
        return true;
    }

}
