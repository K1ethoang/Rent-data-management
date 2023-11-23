package com.example.demo.controller;

import com.example.demo.entity.Customer;
import com.example.demo.message.CustomerMessage;
import com.example.demo.service.implement.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController // @RestController dùng cho API còn @Controller dùng cho return View HTML
@RequestMapping("/customers") // Endpoint gốc là /customers
@AllArgsConstructor
@Log4j2
public class CustomerController {
    private final CustomerService customerService;

    // [GET] /customers
    @GetMapping("") // Endpoint /customers
    // Trả về model là một List<Customer>
    public ResponseEntity<List<Customer>> getCustomerList() {
        List<Customer> customers = customerService.getAll();

        log.info(CustomerMessage.FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    // [GET] /customers/:id
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") String id) {
        Customer customer = customerService.getOneById(id);

        log.info(CustomerMessage.FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    // [POST] /customers/add
    @PostMapping("/add")
    public ResponseEntity<Customer> save(@RequestBody Customer newCustomer) {
        Customer customer = customerService.create(newCustomer);

        log.info(CustomerMessage.SUCCESS_CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    // [POST] /customers/update/:id
    @PostMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody Map<String, Object> payload) {
        Customer customer = customerService.update(id, payload);

        log.info(CustomerMessage.SUCCESS_UPDATED);
        return ResponseEntity.status(HttpStatus.OK).body(CustomerMessage.SUCCESS_UPDATED);
    }


    // [DELETE] /customers/delete/:id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        String message = customerService.delete(id);

        log.info(CustomerMessage.SUCCESS_DELETED);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
