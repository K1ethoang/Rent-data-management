package com.example.demo.service.implement;

import com.example.demo.entity.Apartment;
import com.example.demo.entity.Contract;
import com.example.demo.entity.Customer;
import com.example.demo.entity.User;
import com.example.demo.enumuration.ERole;
import com.example.demo.exception.*;
import com.example.demo.helper.CsvHelper;
import com.example.demo.message.AuthMessage;
import com.example.demo.message.ContractMessage;
import com.example.demo.message.FileMessage;
import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.contract.ContractDTO;
import com.example.demo.model.DTO.contract.ContractUpdateDTO;
import com.example.demo.model.DTO.paging.APIPageableDTO;
import com.example.demo.model.mapper.EntityToDto;
import com.example.demo.repository.ContractRepository;
import com.example.demo.service.interfaces.ApartmentService;
import com.example.demo.service.interfaces.ContractService;
import com.example.demo.service.interfaces.CustomerService;
import com.example.demo.service.interfaces.UserService;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.MyUtils;
import com.example.demo.util.validator.ContractValidator;
import com.example.demo.util.validator.FileValidator;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
@Log4j2
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final CustomerService customerService;
    private final ApartmentService apartmentService;
    private final UserService userService;

    @Override
    public Map<String, Object> getAll(Pageable pageable) throws NoContentException {
        Map<String, Object> result = new HashMap<>();

        Page<Contract> pageEntity = contractRepository.findAll(pageable);
        Page<ContractDTO> pageDto = pageEntity.map(EntityToDto::contractToDto);

        APIPageableDTO apiPageableDTO = new APIPageableDTO(pageDto);

        result.put("page", apiPageableDTO);
        result.put("contracts", pageDto.getContent());

        return result;
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

        checkApartmentInUsing(contractDTO);

        Customer customerFromDB = customerService.getCustomer(contractDTO.getCustomerId());
        Apartment apartmentFromDB = apartmentService.getApartment(contractDTO.getApartmentId());
        User userFromDB = userService.getUserById(contractDTO.getUserId());

        LocalDate startDate = LocalDate.parse(contractDTO.getStartDate());
        LocalDate endDate = LocalDate.parse(contractDTO.getEndDate());

        Contract contractToCreate = Contract.builder()
                .startDate(startDate)
                .endDate(endDate)
                .total(calculateTotal(startDate, endDate, apartmentFromDB))
                .retailPrice(apartmentFromDB.getRetailPrice())
                .customer(customerFromDB)
                .apartment(apartmentFromDB)
                .user(userFromDB)
                .build();

        Contract StoredContract = contractRepository.save(contractToCreate);

        return EntityToDto.contractToDto(StoredContract);
    }

    @Override
    public ContractDTO update(String id, ContractUpdateDTO contractUpdate, String token) throws JwtException, ForbiddenException {
        if (JwtUtil.isAccessTokenExpired(token)) {
            throw new JwtException(AuthMessage.TOKEN_EXPIRED);
        }


        // Kiểm tra có phải user update làm hợp đồng này không
        String username = JwtUtil.extractUsername(token);
        User userFromDb = userService.getUserByUsername(username);

        Contract storedContract = getContract(id);

        if (JwtUtil.extractRole(token).equals(ERole.MANAGER.toString()) || storedContract.getUser().getId().equals(userFromDb.getId())) {
            ContractValidator.validatorContactUpdateDTO(contractUpdate);

            Contract tempContract = storedContract;

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

            checkApartmentInUsing(EntityToDto.contractToDto(tempContract));

            storedContract.setStartDate(tempContract.getStartDate());
            storedContract.setEndDate(tempContract.getEndDate());
            storedContract.setCustomer(tempContract.getCustomer());
            storedContract.setApartment(tempContract.getApartment());

            // Update price and total
            storedContract.setTotal(calculateTotal(storedContract.getStartDate(),
                    storedContract.getEndDate(), storedContract.getApartment()));
            storedContract.setRetailPrice(storedContract.getApartment().getRetailPrice());

            return EntityToDto.contractToDto(contractRepository.save(storedContract));

        } else
            throw new ForbiddenException();
    }

    @Override
    public ContractDTO delete(String id) throws NotFoundException, InValidException {
        Contract contract = getContract(id);

        // Kiểm tra xem hợp đồng này đang còn hiệu lực không

        LocalDate dateNow = LocalDate.parse(MyUtils.getDateNow());
        if (dateNow.isBefore(contract.getStartDate()) || dateNow.isAfter(contract.getEndDate())) {
            contractRepository.delete(contract);
        } else {
            throw new InValidException(ContractMessage.APARTMENT_USING);
        }


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
                checkApartmentInUsing(contractList.get(i));
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

    public void checkApartmentInUsing(ContractDTO contractToCheck) throws InValidException {
        List<Contract> contractListByApartmentId =
                contractRepository.getContractsByApartment_Id(contractToCheck.getApartmentId());

        LocalDate startDateOfNewContract = LocalDate.parse(contractToCheck.getStartDate());
        LocalDate endDateOfNewContract = LocalDate.parse(contractToCheck.getEndDate());

        for (Contract contract : contractListByApartmentId) {
            // kiểm tra xem có phải contract đang check có trong danh sách không
            if (contractToCheck.getId() != null && contractToCheck.getId().equals(contract.getId()))
                continue;

            if (!(endDateOfNewContract.isBefore(contract.getStartDate()) || startDateOfNewContract.isAfter(contract.getEndDate())))
                throw new InValidException(ContractMessage.APARTMENT_USING);
        }
    }

    public double calculateTotal(LocalDate startDate, LocalDate endDate, Apartment apartment) {
        return MyUtils.getMonthBetweenTwoLocalDate(startDate, endDate) * apartment.getRetailPrice();
    }


    @Override
    public File exportCsv(Boolean getTemplate) {
        try {
            List<ContractDTO> contractDTOList = new ArrayList<>();
            contractRepository.findAll().forEach(contract -> contractDTOList.add(EntityToDto.contractToDto(contract)));
            return CsvHelper.exportContracts(contractDTOList, getTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> search(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty())
            throw new InValidException(GlobalMessage.NOT_NULL_QUERY);

        Map<String, Object> result = new HashMap<>();

        Page<Contract> pageEntity = contractRepository.search(query.trim(), pageable);
        Page<ContractDTO> pageDTOS = pageEntity.map(EntityToDto::contractToDto);

        APIPageableDTO apiPageableDTO = new APIPageableDTO(pageDTOS);

        result.put("page", apiPageableDTO);
        result.put("contracts", pageDTOS.getContent());

        return result;
    }
}
