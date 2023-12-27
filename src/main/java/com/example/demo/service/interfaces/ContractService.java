package com.example.demo.service.interfaces;

import com.example.demo.model.DTO.ContractDTO;

import java.util.List;
import java.util.Map;

public interface ContractService {
    List<ContractDTO> getAll();

    ContractDTO getOneById(String id);

    ContractDTO create(ContractDTO contractDTO);

    ContractDTO update(String id, Map<String, Object> payload);

    ContractDTO delete(String id);
}
