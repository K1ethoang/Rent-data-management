package com.example.demo.service.interfaces;

import com.example.demo.entity.User;
import com.example.demo.model.DTO.ChangePasswordDto;
import com.example.demo.model.DTO.user.UserDto;
import com.example.demo.model.DTO.user.UserUpdateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.File;
import java.util.Map;

public interface UserService extends UserDetailsService {
    Map<String, Object> getAll(Pageable pageable);

    void createUser(UserDto userDto);

    void updateUser(String id, UserUpdateDto userUpdateDto, String token);

    void changePassword(String id, ChangePasswordDto userUpdateDto, String token);

    void blockUser(String id);

    void unBlockUser(String id);

    UserDto getUserDetailsFromToken(String token);

    UserDto getUserDetailsFromRefreshToken(String token);

    User getUserByUsername(String username);

    User getUserByUsernameOrEmail(String username);

    UserDto getUserDTO(String userId);

    User getUserById(String userId);

    User getUserByEmail(String email);

    Map<String, Object> statistic();

    File exportCsv(boolean getTemplate);
}
