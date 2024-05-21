package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.ChangePasswordDto;
import com.example.demo.model.DTO.user.UserDto;
import com.example.demo.model.DTO.user.UserUpdateDto;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.interfaces.UserService;
import com.example.demo.util.JwtUtil;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Log4j2
public class UserController {
    private final UserService userService;
    private final String DEFAULT_PAGE_NUMBER = "0";
    private final String DEFAULT_PAGE_SIZE = "10";
    private final String DEFAULT_SORT_BY = "createDate";

    @GetMapping("/details")
    public ResponseEntity<Object> details(HttpServletRequest request) {
        String token = JwtUtil.getTokenFromRequest(request);

        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS,
                userService.getUserDetailsFromToken(token));
    }

    // [GET] /users/
    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("")
    public ResponseEntity<Object> getUserList(
            @RequestParam(defaultValue =
                    DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue =
                    DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue =
                    DEFAULT_SORT_BY) String sortBy) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortBy));

        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS,
                userService.getAll(pageable));
    }

    // [POST] /users/add
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/add")
    public ResponseEntity<Object> save(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ApiResponse.responseBuilder(HttpStatus.CREATED, GlobalMessage.SUCCESS, null);
    }

    // [POST] /users/update/:id
    @PostMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") String id,
                                         @RequestBody UserUpdateDto userUpdateDto, HttpServletRequest request) {
        String token = JwtUtil.getTokenFromRequest(request);
        userService.updateUser(id, userUpdateDto, token);
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, null);
    }

    // [POST] /users/block/:id
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/block/{id}")
    public ResponseEntity<Object> block(@PathVariable("id") String id) {
        userService.blockUser(id);
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, null);
    }

    // [POST] /users/unblock/:id
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/unblock/{id}")
    public ResponseEntity<Object> unblock(@PathVariable("id") String id) {
        userService.unBlockUser(id);
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, null);
    }

    // [POST] /users/changePassword/:id
    @PostMapping("/changePassword/{id}")
    public ResponseEntity<Object> changePassword(@PathVariable("id") String id,
                                                 @RequestBody ChangePasswordDto changePasswordDto
            , HttpServletRequest request) {
        String token = JwtUtil.getTokenFromRequest(request);
        userService.changePassword(id, changePasswordDto, token);
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, null);
    }

    //[GET] /users/statistic
    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/statistic")
    public ResponseEntity<Object> statistic() {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS,
                userService.statistic());
    }

    // [GET] /users/export
    @GetMapping("/export")
    public ResponseEntity<Object> exportCsv(@RequestParam(value = "getTemplate",
            defaultValue = "false") boolean getTemplate) {
        HttpHeaders responseHeader = new HttpHeaders();

        File file = null;
        try {
            file = userService.exportCsv(getTemplate);
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
