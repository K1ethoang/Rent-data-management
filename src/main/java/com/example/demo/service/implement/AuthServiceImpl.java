package com.example.demo.service.implement;

import com.example.demo.entity.User;
import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.message.AuthMessage;
import com.example.demo.message.UserMessage;
import com.example.demo.model.DTO.jwt.JwtResponseDTO;
import com.example.demo.model.DTO.login.LoginDTO;
import com.example.demo.model.DTO.refreshToken.RefreshTokenDTO;
import com.example.demo.model.DTO.register.RegisterDTO;
import com.example.demo.model.DTO.user.UserDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.interfaces.AuthService;
import com.example.demo.service.interfaces.UserService;
import com.example.demo.util.AuthUtils;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.validator.AuthValidator;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final MailServiceImpl mailService;
    private final UserRepository userRepository;

    @Override
    public JwtResponseDTO login(LoginDTO loginDTO) throws InValidException {
        AuthValidator.validatorLoginDTO(loginDTO);

        UserDetails userDetails = null;

        try {
            userDetails = userService.loadUserByUsername(loginDTO.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new InValidException(AuthMessage.CHECK_ACCOUNT);
        }

        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                .accessToken(JwtUtil.createAccessToken(userDetails))
                .refreshToken(null)
                .build();

        UserDto user = userService.getUserDetailsFromToken(jwtResponseDTO.getAccessToken());

        if (!user.isStatus())
            throw new InValidException(AuthMessage.BLOCKED_USER);

        String refreshToken = userService.createRefreshToken(user.getId());

        jwtResponseDTO.setRefreshToken(refreshToken);

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
    public void logout(String token) throws JwtException, NotFoundException {
        if (JwtUtil.isAccessTokenExpired(token)) {
            throw new JwtException(AuthMessage.TOKEN_EXPIRED);
        }

        String username = JwtUtil.extractUsername(token);
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isEmpty())
            throw new NotFoundException(UserMessage.NOT_FOUND);

        User user = userOptional.get();

        user.setExpRefreshToken(null);
        user.setRefreshToken(null);

        SecurityContextHolder.clearContext();

        userRepository.save(user);
    }

    @Override
    public JwtResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
        AuthValidator.validatorRefreshTokenDTO(refreshTokenDTO);

        UserDto user = userService.getUserDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken());

        if (!user.isStatus())
            throw new InValidException(AuthMessage.BLOCKED_USER);

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());

        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                .accessToken(JwtUtil.createAccessToken(userDetails))
                .refreshToken(null)
                .build();


        String refreshToken = userService.updateRefreshToken(user.getId());

        jwtResponseDTO.setRefreshToken(refreshToken);

        return jwtResponseDTO;
    }
}
