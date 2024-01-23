package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.interfaces.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/file")
@AllArgsConstructor
public class FileUploadController {
    private final CustomerService customerService;

    @PostMapping(value = "/upload/customer", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> uploadCustomers(@RequestParam("file") MultipartFile file) {
        customerService.loadCustomers(file);
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS);
    }
}
