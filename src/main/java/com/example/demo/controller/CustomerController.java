package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.CustomerDTO;
import com.example.demo.model.DTO.CustomerUpdateDTO;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.interfaces.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController // @RestController dùng cho API còn @Controller dùng cho return View HTML
@RequestMapping("/customers") // Endpoint gốc là /customers
@AllArgsConstructor
@Log4j2
public class CustomerController {
    private final CustomerService customerService;

    // [GET] /customers
    @GetMapping({"", "/"})
    public ResponseEntity<Object> getCustomerList() {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, customerService.getAll());
    }

    // [GET] /customers/:id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomer(@PathVariable("id") String id) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, customerService.getCustomerDTO(id));
    }

    // [POST] /customers/add
    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody CustomerDTO customerDTO) {
        return ApiResponse.responseBuilder(HttpStatus.CREATED, GlobalMessage.SUCCESS, customerService.create(customerDTO));
    }

    // [POST] /customers/update/:id
    @PostMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody CustomerUpdateDTO customerUpdate) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, customerService.update(id, customerUpdate));
    }

    // [DELETE] /customers/delete/:id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, customerService.delete(id));
    }

    // [POST] /customers/import
    @PostMapping("/import")
    public ResponseEntity<Object> uploadCsvCustomer(@RequestParam("file") MultipartFile[] files) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS,
                customerService.loadCustomers(files));
    }
}
