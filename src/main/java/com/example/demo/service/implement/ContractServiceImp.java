package com.example.demo.service.implement;

import com.example.demo.entity.Apartment;
import com.example.demo.entity.Contract;
import com.example.demo.entity.Customer;
import com.example.demo.exception.DuplicatedException;
import com.example.demo.exception.InValidException;
import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.helpers.CsvHelper;
import com.example.demo.message.ContractMessage;
import com.example.demo.message.FileMessage;
import com.example.demo.model.DTO.ContractDTO;
import com.example.demo.model.DTO.ContractUpdateDTO;
import com.example.demo.model.mapper.EntityToDto;
import com.example.demo.repository.ContractRepository;
import com.example.demo.service.interfaces.ApartmentService;
import com.example.demo.service.interfaces.ContractService;
import com.example.demo.service.interfaces.CustomerService;
import com.example.demo.utils.MyUtils;
import com.example.demo.utils.validator.ContractValidator;
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
public class ContractServiceImp implements ContractService {
    private final ContractRepository contractRepository;
    private final CustomerService customerService;
    private final ApartmentService apartmentService;
//    private final ModelMapper mapper;

    @Override
    public List<ContractDTO> getAll() throws NoContentException {
        List<ContractDTO> contractList = new ArrayList<>();

        contractRepository.findAll().forEach(contract -> contractList.add(EntityToDto.contractToDto(contract)));

        if (contractList.isEmpty()) throw new NoContentException(ContractMessage.EMPTY_LIST);

        return contractList;
    }

    private Contract getContract(String id) throws NotFoundException {
        Optional<Contract> optional = contractRepository.findById(id);

        if (optional.isEmpty()) throw new NotFoundException(ContractMessage.NOT_FOUND);

        return optional.get();
    }

    @Override
    public ContractDTO getContractDTO(String id) {
        Contract contractFromDB = getContract(id);

        return EntityToDto.contractToDto(contractFromDB);
    }

    @Override
    public ContractDTO create(ContractDTO contractDTO) {
        ContractValidator.validatorContractDTO(contractDTO);

        checkDuplicated(contractDTO);

        Customer customerFromDB = customerService.getCustomer(contractDTO.getCustomerId());
        Apartment apartmentFromDB = apartmentService.getApartment(contractDTO.getApartmentId());

        Contract contractToCreate = Contract.builder()
                .startDate(MyUtils.stringToDate(contractDTO.getStartDate()))
                .endDate(MyUtils.stringToDate(contractDTO.getEndDate()))
                .customer(customerFromDB)
                .apartment(apartmentFromDB)
                .build();

        contractRepository.save(contractToCreate);

        return contractDTO;
    }

    @Override
    public ContractDTO update(String id, ContractUpdateDTO contractUpdate) {
        ContractValidator.validatorContactUpdateDTO(contractUpdate);

        Contract storedContract = getContract(id);

        Contract tempContract = Contract.builder()
                .startDate(storedContract.getStartDate())
                .endDate(storedContract.getEndDate())
                .customer(storedContract.getCustomer())
                .apartment(storedContract.getApartment())
                .build();

        if (contractUpdate.getCustomerId() != null) {
            Customer storedCustomer = customerService.getCustomer(contractUpdate.getCustomerId());
            tempContract.setCustomer(storedCustomer);
        }
        if (contractUpdate.getApartmentId() != null) {
            Apartment storedApartment =
                    apartmentService.getApartment(contractUpdate.getApartmentId());
            tempContract.setApartment(storedApartment);
        }
        if (contractUpdate.getStartDate() != null) {
            tempContract.setStartDate(MyUtils.stringToDate(contractUpdate.getStartDate()));
        }
        if (contractUpdate.getEndDate() != null) {
            tempContract.setEndDate(MyUtils.stringToDate(contractUpdate.getEndDate()));
        }

        ContractValidator.invalidStartDateAndEndDate(tempContract.getStartDate(),
                tempContract.getEndDate());

        checkDuplicated(EntityToDto.contractToDto(tempContract));

        storedContract.setStartDate(tempContract.getStartDate());
        storedContract.setEndDate(tempContract.getEndDate());
        storedContract.setCustomer(tempContract.getCustomer());
        storedContract.setApartment(tempContract.getApartment());

        contractRepository.save(storedContract);

        return EntityToDto.contractToDto(storedContract);
    }

    @Override
    public ContractDTO delete(String id) throws NotFoundException {
        Contract contract = getContract(id);

        contractRepository.delete(contract);

        return EntityToDto.contractToDto(contract);
    }

    @Override
    public Map<String, Object> loadContract(MultipartFile file) {
        FileValidator.validatorMultipartFile(file);

        if (!CsvHelper.hasCsvFormat(file)) throw new InValidException(FileMessage.MUST_TYPE_CSV);

        Map<String, Object> response = new HashMap<>();
        response.put("File", file.getOriginalFilename());

        List<ContractDTO> contractList = CsvHelper.csvToContracts(file);

        int numberOfContractAdded = 0;

        String failedRows = "";

        for (int i = 0; i < contractList.size(); i++) {
            try {
                ContractValidator.validatorContractDTO(contractList.get(i));
            } catch (Exception e) {
                failedRows = failedRows.concat((i + 1) + " , ");
                continue;
            }

            Customer customerFromDB;
            Apartment apartmentFromDB;

            try {
                apartmentFromDB = apartmentService.getApartment(contractList.get(i).getApartmentId());
            } catch (NotFoundException e) {
                failedRows = failedRows.concat((i + 1) + " , ");
                continue;
            }

            try {
                customerFromDB = customerService.getCustomer(contractList.get(i).getCustomerId());
            } catch (NotFoundException e) {
                failedRows = failedRows.concat((i + 1) + " , ");
                continue;
            }

            try {
                checkDuplicated(contractList.get(i));
            } catch (DuplicatedException e) {
                failedRows = failedRows.concat((i + 1) + " , ");
                continue;
            }

            Contract contractToAdd = Contract.builder()
                    .startDate(MyUtils.stringToDate(contractList.get(i).getStartDate()))
                    .endDate(MyUtils.stringToDate(contractList.get(i).getEndDate()))
                    .customer(customerFromDB)
                    .apartment(apartmentFromDB)
                    .build();

            contractRepository.save(contractToAdd);
            numberOfContractAdded++;
        }

        response.put(FileMessage.NUMBER_SUCCESS_ROW, numberOfContractAdded);

        if (!failedRows.isEmpty())
            failedRows = failedRows.substring(0, failedRows.length() - 3);
        response.put(FileMessage.FAILED_ROWS, failedRows);

        return response;
    }

    @Override
    public List<Object> loadContracts(MultipartFile[] files) {
        List<Object> response = new ArrayList<>();

        for (MultipartFile file : files) {
            response.add(loadContract(file));
        }

        return response;
    }

    public void checkDuplicated(ContractDTO contractToCheck) throws DuplicatedException {
        List<Contract> contractList = contractRepository.findAll();

        boolean isDuplicate = false;

        for (Contract contract : contractList) {
            ContractDTO contractFromList = EntityToDto.contractToDto(contract);

            if (!contractFromList.getStartDate().equals(contractToCheck.getStartDate())) continue;
            if (!contractFromList.getEndDate().equals(contractToCheck.getEndDate())) continue;
            if (!contractFromList.getCustomerId().equals(contractToCheck.getCustomerId())) continue;
            if (!contractFromList.getApartmentId().equals(contractToCheck.getApartmentId()))
                continue;

            isDuplicate = true;
            break;
        }

        if (isDuplicate) throw new DuplicatedException(ContractMessage.CONTRACT_EXIST);
    }

    @Override
    public File exportCsv(Boolean getTemplate) {
        try {
            List<ContractDTO> contractList = new ArrayList<>();
            contractRepository.findAll().forEach(contract -> contractList.add(EntityToDto.contractToDto(contract)));

            return CsvHelper.exportContracts(contractList, getTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
