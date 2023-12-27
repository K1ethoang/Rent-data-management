package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.ContractDTO;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.interfaces.ContractService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/contracts")
public class ContractController {
    public final ContractService contractService;

    // [GET] : /contracts/
    @GetMapping({"", "/"})
    public ResponseEntity<Object> getContractList() {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, contractService.getAll());
    }

    // [GET] : /contracts/:id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getContract(@PathVariable("id") String id) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, contractService.getOneById(id));
    }

    // [POST] : contracts/add
    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody ContractDTO contractDTO) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, contractService.create(contractDTO));
    }

    // [UPDATE] : contracts/update/:id
    @PostMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody Map<String, Object> payload) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, contractService.update(id, payload));
    }

    // [DELETE] : contracts/delete/:id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, contractService.delete(id));
    }
}
