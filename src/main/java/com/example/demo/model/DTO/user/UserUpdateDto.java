package com.example.demo.model.DTO.user;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String email;
    private String username;
    private String fullName;
    private Boolean active;
    private String role;
}
