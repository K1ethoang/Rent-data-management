package com.example.demo.controller;

import com.example.demo.entity.Apartment;
import com.example.demo.service.implement.ApartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("apartments")
public class ApartmentController {
    private final ApartmentService apartmentService;

    // [GET] /apartments/
    @GetMapping("") // Endpoint /apartments
    public ResponseEntity<List<Apartment>> getApartmentList() {
        try {
            List<Apartment> apartments = apartmentService.getAll();

            if (apartments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.status(200).body(apartments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // [GET] /apartments/:id
    @GetMapping("{id}")
    public ResponseEntity<Apartment> getApartment(@PathVariable("id") String id) {
        try {
            Apartment apartment = apartmentService.getOne(id);

            if (apartment == null) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            return ResponseEntity.status(200).body(apartment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // [POST] /apartments/add
    @PostMapping("add")
    public ResponseEntity<Apartment> save(@RequestBody Apartment newApartment) {
        try {
            Apartment apartment = apartmentService.create(newApartment);

            if (apartment == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            return ResponseEntity.status(200).body(apartment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // [POST] /apartments/update/:id
    @PostMapping("update/{id}")
    public ResponseEntity<Apartment> update(@PathVariable("id") String id, @RequestBody Map<String, Object> payload) {
        try {
            Apartment apartment = apartmentService.update(id, payload);

            if (apartment == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

            return ResponseEntity.status(200).body(apartment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // [DELETE] /apartments/delete/:id
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        try {
            apartmentService.delete(id);

            return ResponseEntity.status(200).body("Apartment successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
