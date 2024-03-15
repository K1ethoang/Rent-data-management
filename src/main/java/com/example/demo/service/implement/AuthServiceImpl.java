package com.example.demo.service.implement;

import com.example.demo.exception.InValidException;
import com.example.demo.model.DTO.login.LoginDTO;
import com.example.demo.model.DTO.register.RegisterDTO;
import com.example.demo.model.DTO.user.UserDto;
import com.example.demo.service.interfaces.AuthService;
import com.example.demo.service.interfaces.UserService;
import com.example.demo.utils.AuthUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;

    @Override
    public void login(LoginDTO loginDTO) {

    }

    @Override
    public void register(RegisterDTO registerDTO) throws InValidException {
        String passwordGenerator = AuthUtils.generatePassword();

        UserDto userDto = UserDto.builder()
                .email(registerDTO.getEmail())
                .username(registerDTO.getUsername())
                .password(passwordGenerator)
                .build();

        userService.createUser(userDto);
    }
}
