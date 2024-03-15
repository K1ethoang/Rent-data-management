package com.example.demo.model.DTO.login;

import lombok.Data;

@Data
public class LoginDTO {
    private String emailOrUsername;
    private String password;
}
