package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.ApartmentDTO;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.interfaces.ApartmentService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("apartments")
@Log4j2
public class ApartmentController {
    private final ApartmentService apartmentService;

    // [GET] /apartments/
    @GetMapping({"", "/"})
    public ResponseEntity<Object> getApartmentList() {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, apartmentService.getAll());
    }

    // [GET] /apartments/:id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getApartment(@PathVariable("id") String id) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, apartmentService.getOneById(id));
    }

    // [POST] /apartments/add
    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody ApartmentDTO apartmentDTO) {
        return ApiResponse.responseBuilder(HttpStatus.CREATED, GlobalMessage.SUCCESS, apartmentService.create(apartmentDTO));
    }

    // [POST] /apartments/update/:id
    @PostMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody Map<String, Object> payload) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, apartmentService.update(id, payload));
    }

    // [DELETE] /apartments/delete/:id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, apartmentService.delete(id));
    }
}
