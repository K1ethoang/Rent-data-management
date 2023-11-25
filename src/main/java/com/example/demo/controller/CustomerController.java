package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.CustomerDTO;
import com.example.demo.response.ResponseHandler;
import com.example.demo.service.implement.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // @RestController dùng cho API còn @Controller dùng cho return View HTML
@RequestMapping("/customers") // Endpoint gốc là /customers
@AllArgsConstructor
@Log4j2
public class CustomerController {
    private final CustomerService customerService;

    // [GET] /customers
    @GetMapping("")
    public ResponseEntity<Object> getCustomerList() {
        return ResponseHandler.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, customerService.getAll());
    }

    // [GET] /customers/:id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomer(@PathVariable("id") String id) {
        return ResponseHandler.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, customerService.getOneById(id));
    }

    // [POST] /customers/add
    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody CustomerDTO customerDTO) {
        return ResponseHandler.responseBuilder(HttpStatus.CREATED, GlobalMessage.SUCCESS, customerService.create(customerDTO));
    }

    // [POST] /customers/update/:id
    @PostMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody Map<String, Object> payload) {
        return ResponseHandler.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, customerService.update(id, payload));
    }

    // [DELETE] /customers/delete/:id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        return ResponseHandler.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, customerService.delete(id));
    }

}
