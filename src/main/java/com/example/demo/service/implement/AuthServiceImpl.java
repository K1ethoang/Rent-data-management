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
import com.example.demo.service.interfaces.TokenService;
import com.example.demo.service.interfaces.UserService;
import com.example.demo.util.AuthUtils;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.validator.AuthValidator;
import io.jsonwebtoken.JwtException;
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
    private final TokenService tokenService;
    private final MailServiceImpl mailService;
    private final UserRepository userRepository;

    @Override
    public JwtResponseDTO login(LoginDTO loginDTO) throws InValidException {
        AuthValidator.validatorLoginDTO(loginDTO);

        User user = null;
        try {
            user = userService.getUserByUsernameOrEmail(loginDTO.getUsername());

        } catch (NotFoundException e) {
            throw new InValidException(AuthMessage.CHECK_ACCOUNT);
        }

        if (!AuthUtils.isValidPassword(loginDTO.getPassword(), user.getPassword()))
            throw new InValidException(AuthMessage.CHECK_ACCOUNT);

        if (!user.getActive())
            throw new InValidException(AuthMessage.BLOCKED_USER);

        String refreshToken = tokenService.createRefreshToken(user);
        String accessToken =
                JwtUtil.createAccessToken(userService.loadUserByUsername(user.getUsername()));

        tokenService.updateAccessToken(user.getId(), accessToken);

        return JwtResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        AuthValidator.validatorRegisterDTO(registerDTO);

        UserDto userDto = UserDto.builder()
                .email(registerDTO.getEmail())
                .fullName(registerDTO.getFullName())
                .password(AuthUtils.generatePassword())
                .build();
        try {
            userService.createUser(userDto);
            mailService.sendMailRegisterSuccess(userDto);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void logout(String token) throws JwtException, NotFoundException, InValidException {
        String username = JwtUtil.extractUsername(token);
        User user = userService.getUserByUsername(username);

        String tokenFromDb = user.getAccessToken();

        if (tokenFromDb != null && tokenFromDb.equals(token)) {
            user.setAccessToken(null);
            user.setRefreshToken(null);
            user.setExpRefreshToken(null);
            userRepository.save(user);
        } else
            throw new InValidException(AuthMessage.TOKEN_EXPIRED);
    }

    @Override
    public JwtResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
        AuthValidator.validatorRefreshTokenDTO(refreshTokenDTO);

        UserDto user = userService.getUserDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken());

        if (!user.getActive())
            throw new InValidException(AuthMessage.BLOCKED_USER);

        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());

        String refreshToken = tokenService.updateRefreshToken(user.getId());
        String accessToken = JwtUtil.createAccessToken(userDetails);

        JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        tokenService.updateAccessToken(user.getId(), accessToken);

        return jwtResponseDTO;
    }

    @Override
    public void resetPassword(String email) throws InValidException, NotFoundException {
        if (!AuthUtils.isValidEmail(email))
            throw new InValidException(UserMessage.EMAIL_INVALID);

        // Tìm xem có user nào với email này không
        User userFromDb = userService.getUserByEmail(email);

        String newPassword = AuthUtils.generatePassword();

        // Gửi mail
        try {
            // Cập nhật mật khẩu mới vào database
            userFromDb.setPassword(AuthUtils.encodePassword(newPassword));
            userRepository.save(userFromDb);

            mailService.sendMailNewPassword(email, newPassword);
        } catch (MessagingException e) {
            throw new RuntimeException(AuthMessage.ERROR_SEND_MAIL);
        }
    }
}
