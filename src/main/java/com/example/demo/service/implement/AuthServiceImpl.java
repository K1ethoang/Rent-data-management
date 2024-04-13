package com.example.demo.service.implement;

import com.example.demo.exception.InValidException;
import com.example.demo.model.DTO.jwt.JwtResponseDTO;
import com.example.demo.model.DTO.login.LoginDTO;
import com.example.demo.model.DTO.register.RegisterDTO;
import com.example.demo.model.DTO.user.UserDto;
import com.example.demo.service.interfaces.AuthService;
import com.example.demo.service.interfaces.UserService;
import com.example.demo.util.AuthUtils;
import com.example.demo.util.validator.AuthValidator;
import com.example.demo.util.validator.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@AllArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final MailServiceImpl mailService;

    @Override
    public JwtResponseDTO login(LoginDTO loginDTO) throws InValidException {
        AuthValidator.validatorLoginDTO(loginDTO);

        UserDetails userDetails = userService.loadUserByUsername(loginDTO.getUsername());

        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO().builder()
                .accessToken(JwtUtil.createAccessToken(userDetails))
                .refreshToken(null)
                .build();

        return jwtResponseDTO;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        AuthValidator.validatorRegisterDTO(registerDTO);

        UserDto userDto = UserDto.builder()
                .email(registerDTO.getEmail())
                .username(registerDTO.getUsername())
                .password(AuthUtils.generatePassword())
                .build();
        try {
            userService.createUser(userDto);
            mailService.sendMail(userDto);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void logout() {

    }
}
