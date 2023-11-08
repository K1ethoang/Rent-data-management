package com.example.demo.controller;

import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // @RestController dùng cho API còn @Controller dùng cho return View HTML
@RequestMapping("/customers") // Endpoint gốc là /customers
@AllArgsConstructor
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/get") // Endpoint /customers/get
    // Trả về model là một List<Customer>
    public ResponseEntity<List<Customer>> getCustomerList(){
        List<Customer> customers = customerService.getAll();
        return ResponseEntity.ok(customers);
    }
}
