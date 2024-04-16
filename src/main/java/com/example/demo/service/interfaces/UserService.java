package com.example.demo.service.interfaces;

import com.example.demo.model.DTO.user.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void createUser(UserDto userDto);

    UserDto getUserDetailsFromToken(String authorizationHeader);
}
