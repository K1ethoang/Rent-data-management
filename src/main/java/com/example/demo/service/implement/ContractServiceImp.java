package com.example.demo.service.implement;

import com.example.demo.entity.Apartment;
import com.example.demo.entity.Contract;
import com.example.demo.entity.Customer;
import com.example.demo.exception.InValidException;
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

    public void checkDuplicated(ContractDTO contractToCheck) throws InValidException {
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

        if (isDuplicate) throw new InValidException(ContractMessage.CONTRACT_EXIST);
    }

//    private Contract contractDTOToEntity(ContractDTO contractDTO) {
//        return mapper.map(contractDTO, Contract.class);
//    }
//
//    private ContractDTO contractEntityToDTO(Contract contract) {
//        return mapper.map(contract, ContractDTO.class);
//    }


}
