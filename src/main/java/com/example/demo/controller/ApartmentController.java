package com.example.demo.controller;

import com.example.demo.entity.Apartment;
import com.example.demo.message.ApartmentMessage;
import com.example.demo.service.implement.ApartmentService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("apartments")
@Log4j2
public class ApartmentController {
    private final ApartmentService apartmentService;

    // [GET] /apartments/
    @GetMapping("") // Endpoint /apartments
    public ResponseEntity<Object> getApartmentList() {
        List<Apartment> apartments = apartmentService.getAll();

        return ResponseEntity.status(HttpStatus.OK).body(apartments);
    }

    // [GET] /apartments/:id
    @GetMapping("{id}")
    public ResponseEntity<Object> getApartment(@PathVariable("id") String id) {
        Apartment apartment = apartmentService.getOne(id);

        return ResponseEntity.status(HttpStatus.OK).body(apartment);
    }

    // [POST] /apartments/add
    @PostMapping("add")
    public ResponseEntity<Object> save(@RequestBody Apartment newApartment) {
        Apartment apartment = apartmentService.create(newApartment);

        log.info(ApartmentMessage.SUCCESS_CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(apartment);
    }

    // [POST] /apartments/update/:id
    @PostMapping("update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody Map<String, Object> payload) {
        String message = apartmentService.update(id, payload);

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // [DELETE] /apartments/delete/:id
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        String message = apartmentService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
