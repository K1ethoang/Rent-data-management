package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.contract.ContractDTO;
import com.example.demo.model.DTO.contract.ContractUpdateDTO;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.interfaces.ContractService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/contracts")
public class ContractController {
    public final ContractService contractService;
    private final String DEFAULT_PAGE_NUMBER = "0";
    private final String DEFAULT_PAGE_SIZE = "10";
    private final String DEFAULT_SORT_BY = "endDate";

    // [GET] : /contracts/
    @GetMapping("")
    public ResponseEntity<Object> getContractList(@RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                                  @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortBy));

        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS,
                contractService.getAll(pageable));
    }

    // [GET] : /contracts/:id
    @GetMapping("/{id}")
    public ResponseEntity<Object> getContract(@PathVariable("id") String id) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, contractService.getContractDTO(id));
    }

    // [POST] : contracts/add
    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody ContractDTO contractDTO) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, contractService.create(contractDTO));
    }

    // [UPDATE] : contracts/update/:id
    @PostMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody ContractUpdateDTO contractUpdate) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, contractService.update(id, contractUpdate));
    }

    // [DELETE] : contracts/delete/:id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, contractService.delete(id));
    }

    // [POST] : contracts/import
    @PostMapping("/import")
    public ResponseEntity<Object> importCsvContract(@RequestParam("file") MultipartFile[] files) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS,
                contractService.loadContracts(files));
    }

    // [GET] : /contracts/search
    @GetMapping("/search")
    public ResponseEntity<Object> searchContracts(@RequestParam(defaultValue =
            DEFAULT_PAGE_NUMBER) int page,
                                                  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                                  @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
                                                  @RequestParam("q") String query) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortBy));

        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS,
                contractService.search(query, pageable));
    }

    // [GET] : contracts/export
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportCsv(@RequestParam(value = "getTemplate",
            defaultValue = "false") Boolean getTemplate) {
        HttpHeaders responseHeaders = new HttpHeaders();
        File file = null;

        try {
            file = contractService.exportCsv(getTemplate);
            byte[] data = FileUtils.readFileToByteArray(file);


            responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseHeaders.setContentDisposition(ContentDisposition.attachment().filename(file.getName()).build());

            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

            return new ResponseEntity<>(inputStreamResource, responseHeaders, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (file != null)
                file.delete();
        }
    }
}
