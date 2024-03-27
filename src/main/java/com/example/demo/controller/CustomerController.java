package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.customer.CustomerDTO;
import com.example.demo.model.DTO.customer.CustomerUpdateDTO;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.interfaces.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController // @RestController dùng cho API còn @Controller dùng cho return View HTML
@AllArgsConstructor
@Log4j2
@RequestMapping("/customers") // Endpoint gốc là /customers
public class CustomerController {
    private final CustomerService customerService;
    private final String DEFAULT_PAGE_NUMBER = "0";
    private final String DEFAULT_PAGE_SIZE = "10";
    private final String DEFAULT_SORT_BY = "age";

    // [GET] /customers
    @GetMapping("")
    public ResponseEntity<Object> getCustomerList(@RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                                  @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortBy));

        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, customerService.getAll(pageable));
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
    public ResponseEntity<Object> importCsvCustomer(@RequestParam("file") MultipartFile[] files) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS,
                customerService.loadCustomers(files));
    }

    // [GET] /customers/search
    @GetMapping("/search")
    public ResponseEntity<Object> searchCustomers(@RequestParam("q") String query,
                                                  @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                                  @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortBy));

        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS,
                customerService.search(query, pageable));
    }

    // [GET] /customers/export
    @GetMapping("/export")
    public ResponseEntity<Object> exportCsv(@RequestParam(value = "getTemplate",
            defaultValue = "false") boolean getTemplate) {
        HttpHeaders responseHeader = new HttpHeaders();

        File file = null;
        try {
            file = customerService.exportCsv(getTemplate);
            byte[] data = FileUtils.readFileToByteArray(file);

            responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeader.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=" + file.getName());
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

            return new ResponseEntity<>(inputStreamResource, responseHeader, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (file != null)
                file.delete();
        }
    }
}
