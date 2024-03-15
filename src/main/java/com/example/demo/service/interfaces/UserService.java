package com.example.demo.service.interfaces;

import com.example.demo.entity.User;
import com.example.demo.model.DTO.user.UserDto;

import java.util.Optional;

public interface UserService {
    void createUser(UserDto userDto);

    Optional<User> getUserByEmailOrUsername(UserDto userDto);
}
