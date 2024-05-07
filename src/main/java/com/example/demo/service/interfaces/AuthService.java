package com.example.demo.service.interfaces;

import com.example.demo.model.DTO.jwt.JwtResponseDTO;
import com.example.demo.model.DTO.login.LoginDTO;
import com.example.demo.model.DTO.refreshToken.RefreshTokenDTO;
import com.example.demo.model.DTO.register.RegisterDTO;

public interface AuthService {
    JwtResponseDTO login(LoginDTO loginDTO);

    void register(RegisterDTO registerDTO);

    void logout(String token);

    JwtResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO);

    void resetPassword(String email);
}
