package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.login.LoginDTO;
import com.example.demo.model.DTO.register.RegisterDTO;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.interfaces.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(RegisterDTO registerDTO) throws Exception {
        authService.register(registerDTO);
        return ApiResponse.responseBuilder(HttpStatus.CREATED, GlobalMessage.SUCCESS, null);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(LoginDTO loginDTO) {
        authService.login(loginDTO);
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, null);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout() {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, null);
    }
}
