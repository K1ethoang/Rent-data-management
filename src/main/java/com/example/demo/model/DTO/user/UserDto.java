package com.example.demo.model.DTO.user;

import com.example.demo.entity.EState;
import lombok.Builder;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
@Data
public class UserDto implements Serializable {
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    private String avatar;
    private LocalDate createDate;
    private EState state;
}