package com.example.demo.controller;

import com.example.demo.entity.Contract;
import com.example.demo.message.ContractMessage;
import com.example.demo.service.implement.ContractServiceImp;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/contracts")
public class ContractController {
    public final ContractServiceImp contractServiceImp;

    // [GET] : /contracts/
    @GetMapping("")
    public ResponseEntity<List<Contract>> getContractList() {
        List<Contract> contracts = contractServiceImp.getAll();

        log.info(ContractMessage.FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(contracts);
    }

    // [GET] : /contracts/:id
    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContract(@PathVariable("id") String id) {
        Contract contract = contractServiceImp.getOneById(id);

        log.info(ContractMessage.FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(contract);
    }

    // [POST] : contracts/add
    @PostMapping("/add")
    public ResponseEntity<Contract> save(@RequestBody Contract newContract) {
        Contract contract = contractServiceImp.create(newContract);

        log.info(ContractMessage.SUCCESS_CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(contract);
    }

    // [DELETE] : contracts/delete/:id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> save(@PathVariable String id) {
        String message = contractServiceImp.delete(id);

        log.info(ContractMessage.SUCCESS_DELETED);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
