package com.example.demo.service.interfaces;

import com.example.demo.entity.Contract;

import java.util.List;
import java.util.Map;

public interface ContractInterface {
    List<Contract> getAll();

    Contract getOneById(String id);

    Contract create(Contract contract);

    Contract update(String id, Map<String, Object> payload);

    String delete(String id);
}
