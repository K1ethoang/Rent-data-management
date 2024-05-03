package com.example.demo.model.DTO.user;

import com.example.demo.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    private String fullname;
    private String createDate;
    private Boolean active;
    private Role role;
}