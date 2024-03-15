package com.example.demo.model.DTO.register;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDTO {
    private String email;
    private String username;
}
