package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.ApartmentDTO;
import com.example.demo.model.DTO.ApartmentUpdateDTO;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.interfaces.ApartmentService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, apartmentService.getApartmentDTO(id));
    }

    // [POST] /apartments/add
    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody ApartmentDTO apartmentDTO) {
        return ApiResponse.responseBuilder(HttpStatus.CREATED, GlobalMessage.SUCCESS, apartmentService.create(apartmentDTO));
    }

    // [POST] /apartments/update/:id
    @PostMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody ApartmentUpdateDTO apartmentUpdateDTO) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, apartmentService.update(id, apartmentUpdateDTO));
    }

    // [DELETE] /apartments/delete/:id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, apartmentService.delete(id));
    }

    // [POST] /apartments/import
    @PostMapping("/import")
    public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile[] files) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS,
                apartmentService.loadApartments(files));
    }

}
