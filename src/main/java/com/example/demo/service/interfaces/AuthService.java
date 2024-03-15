package com.example.demo.service.interfaces;

import com.example.demo.model.DTO.login.LoginDTO;
import com.example.demo.model.DTO.register.RegisterDTO;

public interface AuthService {
    void login(LoginDTO loginDTO);

    void register(RegisterDTO registerDTO);
}
