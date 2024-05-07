package com.example.demo.service.interfaces;

import com.example.demo.model.DTO.contract.ContractDTO;
import com.example.demo.model.DTO.contract.ContractUpdateDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ContractService {
    Map<String, Object> getAll(Pageable pageable);

    ContractDTO getContractDTO(String id);

    ContractDTO create(ContractDTO contractDTO);

    ContractDTO update(String id, ContractUpdateDTO contractUpdate, String token);

    ContractDTO delete(String id);

    Map<String, Object> loadContract(MultipartFile file);

    List<Object> loadContracts(MultipartFile[] files);

    File exportCsv(Boolean getTemplate);

    Map<String, Object> search(String query, Pageable pageable);
}
