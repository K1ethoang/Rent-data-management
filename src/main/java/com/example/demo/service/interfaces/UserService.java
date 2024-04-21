package com.example.demo.service.interfaces;

import com.example.demo.entity.User;
import com.example.demo.model.DTO.user.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void createUser(UserDto userDto);

    UserDto getUserDetailsFromToken(String token);

    UserDto getUserDetailsFromRefreshToken(String token);

    UserDto getUserByUsername(String username);

    UserDto getUserDTO(String userId);

    User getUser(String userId);
}
