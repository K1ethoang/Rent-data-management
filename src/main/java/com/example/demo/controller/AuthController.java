package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.model.DTO.login.LoginDTO;
import com.example.demo.model.DTO.refreshToken.RefreshTokenDTO;
import com.example.demo.model.DTO.register.RegisterDTO;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.implement.LogoutServiceImpl;
import com.example.demo.service.interfaces.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Log4j2
public class AuthController {
    private final AuthService authService;
    private final LogoutServiceImpl logoutService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return ApiResponse.responseBuilder(HttpStatus.CREATED, GlobalMessage.SUCCESS, null);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, authService.login(loginDTO));
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout() {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS, null);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Object> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS,
                authService.refreshToken(refreshTokenDTO));
    }
}
