package com.example.demo.service.implement;

import com.example.demo.entity.Apartment;
import com.example.demo.entity.Contract;
import com.example.demo.entity.Customer;
import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.message.ContractMessage;
import com.example.demo.model.DTO.ContractDTO;
import com.example.demo.model.DTO.ContractUpdateDTO;
import com.example.demo.model.mapper.EntityToDto;
import com.example.demo.repository.ContractRepository;
import com.example.demo.service.interfaces.ApartmentService;
import com.example.demo.service.interfaces.ContractService;
import com.example.demo.service.interfaces.CustomerService;
import com.example.demo.utils.MyUtils;
import com.example.demo.utils.validator.ContractValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<Contract> contractList = contractRepository.findAll();
        if (contractList.isEmpty()) throw new NoContentException(ContractMessage.EMPTY_LIST);

        List<ContractDTO> resultList = new ArrayList<>();

        for (Contract contract : contractList) {
            resultList.add(EntityToDto.contractToDto(contract));
        }

        return resultList;
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

        Customer customerFromDB = customerService.getCustomer(contractDTO.getCustomerId().trim());
        Apartment apartmentFromDB = apartmentService.getApartment(contractDTO.getApartmentId().trim());

        Contract contractToCreate = Contract.builder()
                .startDate(MyUtils.stringToDate(contractDTO.getStartDate().trim()))
                .endDate(MyUtils.stringToDate(contractDTO.getEndDate().trim()))
                .customer(customerFromDB)
                .apartment(apartmentFromDB)
                .build();

        contractRepository.save(contractToCreate);

        return contractDTO;
    }

    @Override
    public ContractDTO update(String id, ContractUpdateDTO contractUpdate) {
        // Validate dữ liệu input
        ContractValidator.validatorContactUpdateDTO(contractUpdate);

        Contract contractFromDB = getContract(id);

        if (contractUpdate.getCustomerId() != null) {
            Customer customerFromDB = customerService.getCustomer(contractUpdate.getCustomerId().trim());
            contractFromDB.setCustomer(customerFromDB);
        }

        if (contractUpdate.getApartmentId() != null) {
            Apartment apartmentFromDB = apartmentService.getApartment(contractUpdate.getApartmentId().trim());
            contractFromDB.setApartment(apartmentFromDB);
        }

        if (contractUpdate.getStartDate() != null) {
            contractFromDB.setStartDate(MyUtils.stringToDate(contractUpdate.getStartDate().trim()));
        }

        if (contractUpdate.getEndDate() != null) {
            contractFromDB.setEndDate(MyUtils.stringToDate(contractUpdate.getEndDate().trim()));
        }

        // Validate sau khi cập nhật các field
        ContractValidator.validatorContractDTO(EntityToDto.contractToDto(contractFromDB));

        contractRepository.save(contractFromDB);

        return EntityToDto.contractToDto(contractFromDB);
    }

    @Override
    public ContractDTO delete(String id) throws NotFoundException {
        Contract contract = getContract(id);

        contractRepository.delete(contract);

        return EntityToDto.contractToDto(contract);
    }

//    private Contract contractDTOToEntity(ContractDTO contractDTO) {
//        return mapper.map(contractDTO, Contract.class);
//    }
//
//    private ContractDTO contractEntityToDTO(Contract contract) {
//        return mapper.map(contract, ContractDTO.class);
//    }


}
