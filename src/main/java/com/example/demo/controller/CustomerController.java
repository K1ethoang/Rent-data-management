package com.example.demo.controller;

import com.example.demo.entity.Customer;
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
        log.info("GET /customers");
        log.trace("GET /customers");
        try {
            List<Customer> customers = customerService.getAll();

            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.status(200).body(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // [GET] /customers/:id
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") String id) {
        try {
            Customer customer = customerService.getOneById(id);

            if (customer == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.status(200).body(customer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // [POST] /customers/add
    @PostMapping("/add")
    public ResponseEntity<Customer> save(@RequestBody Customer newCustomer) {
        try {
            Customer customer = customerService.create(newCustomer);

            if (customer == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            return ResponseEntity.status(200).body(customer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // [PATCH] /customers/update/:id
    @PostMapping("/update/{id}")
    public ResponseEntity<Customer> update(@PathVariable("id") String id, @RequestBody Map<String, Object> payload) {
        try {
            Customer customer = customerService.update(id, payload);

            return ResponseEntity.status(HttpStatus.OK).body(customer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // [DELETE] /customers/delete/:id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        try {
            customerService.delete(id);

            return ResponseEntity.status(200).body("Customer successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
