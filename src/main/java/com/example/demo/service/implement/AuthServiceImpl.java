package com.example.demo.service.implement;

import com.example.demo.entity.User;
import com.example.demo.exception.InValidException;
import com.example.demo.message.AuthMessage;
import com.example.demo.model.DTO.login.LoginDTO;
import com.example.demo.model.DTO.register.RegisterDTO;
import com.example.demo.model.DTO.user.UserDto;
import com.example.demo.service.interfaces.AuthService;
import com.example.demo.service.interfaces.UserService;
import com.example.demo.utils.AuthUtils;
import com.example.demo.utils.validator.AuthValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;

    @Override
    public void login(LoginDTO loginDTO) throws InValidException {
        AuthValidator.validatorLoginDTO(loginDTO);

        UserDto userDto = UserDto.builder()
                .email(loginDTO.getEmailOrUsername())
                .username(loginDTO.getEmailOrUsername())
                .password(loginDTO.getPassword())
                .build();

        Optional<User> optionalUser = userService.getUserByEmailOrUsername(userDto);

        if (optionalUser.isEmpty()) throw new InValidException(AuthMessage.CHECK_ACCOUNT);

        User user = optionalUser.get();

        if (!AuthUtils.isValidPassword(loginDTO.getPassword(), user.getPassword()))
            throw new InValidException(AuthMessage.CHECK_ACCOUNT);
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        AuthValidator.validatorRegisterDTO(registerDTO);

        String passwordGenerator = AuthUtils.generatePassword();

        UserDto userDto = UserDto.builder()
                .email(registerDTO.getEmail())
                .username(registerDTO.getUsername())
                .password(AuthUtils.encryptPassword(passwordGenerator))
                .build();

        userService.createUser(userDto);
    }
}
