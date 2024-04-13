package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.login.LoginDTO;
import com.example.demo.model.DTO.register.RegisterDTO;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.interfaces.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Log4j2
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return ApiResponse.responseBuilder(HttpStatus.CREATED, GlobalMessage.SUCCESS, null);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, authService.login(loginDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout() {
        authService.logout();
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, null);
    }
}
