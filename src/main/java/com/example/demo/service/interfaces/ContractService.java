package com.example.demo.service.interfaces;

import com.example.demo.model.DTO.ContractDTO;
import com.example.demo.model.DTO.ContractUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ContractService {
    List<ContractDTO> getAll();

    ContractDTO getContractDTO(String id);

    ContractDTO create(ContractDTO contractDTO);

    ContractDTO update(String id, ContractUpdateDTO contractUpdate);

    ContractDTO delete(String id);

    Map<String, Object> loadContract(MultipartFile file);

    List<Object> loadContracts(MultipartFile[] files);

    File exportCsv(Boolean getTemplate);
}
