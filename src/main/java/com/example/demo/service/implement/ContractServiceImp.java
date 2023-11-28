package com.example.demo.service.implement;

import com.example.demo.entity.Contract;
import com.example.demo.exception.AppException;
import com.example.demo.message.ContractMessage;
import com.example.demo.repository.ContractRepository;
import com.example.demo.service.interfaces.ContractService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ContractServiceImp implements ContractService {
    public final ContractRepository contractRepository;

    @Override
    public List<Contract> getAll() throws AppException {
        List<Contract> contracts = contractRepository.findAll();

        if (contracts.isEmpty()) throw new AppException(HttpStatus.OK, ContractMessage.EMPTY_LIST);
        return contracts;
    }

    @Override
    public Contract getOneById(String id) throws AppException {
        Contract contract = contractRepository.findById(id).orElse(null);

        if (contract != null) return contract;
        else throw new AppException(HttpStatus.NOT_FOUND, ContractMessage.NOT_FOUND);
    }

    @Override
    public Contract create(Contract contract) {
        return contractRepository.save(contract);
    }

    @Override
    public Contract update(String id, Map<String, Object> payload) {
        return null;
    }

    @Override
    public String delete(String id) {
        Contract contract = contractRepository.findById(id).orElse(null);

        if (contract != null) {
            contractRepository.delete(contract);
            return ContractMessage.SUCCESS_DELETED;
        } else throw new AppException(HttpStatus.NOT_FOUND, ContractMessage.NOT_FOUND);
    }
}
