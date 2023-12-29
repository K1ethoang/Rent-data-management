package com.example.demo.service.interfaces;

import com.example.demo.model.DTO.ContractDTO;
import com.example.demo.model.DTO.ContractUpdateDTO;

import java.util.List;

public interface ContractService {
    List<ContractDTO> getAll();

    ContractDTO getContractDTO(String id);

    ContractDTO create(ContractDTO contractDTO);

    ContractDTO update(String id, ContractUpdateDTO contractUpdate);

    ContractDTO delete(String id);
}
