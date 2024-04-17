package com.example.demo.util.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotNullException;
import com.example.demo.message.AuthMessage;
import com.example.demo.message.UserMessage;
import com.example.demo.model.DTO.login.LoginDTO;
import com.example.demo.model.DTO.refreshToken.RefreshTokenDTO;
import com.example.demo.model.DTO.register.RegisterDTO;
import com.example.demo.util.AuthUtils;

public class AuthValidator {
    public static void notNullEmail(String email) throws NotNullException {
        if (email == null || email.trim().isEmpty())
            throw new NotNullException(UserMessage.EMAIL_REQUIRED);
    }

    public static void notNullUsername(String username) throws NotNullException {
        if (username == null || username.trim().isEmpty())
            throw new NotNullException(AuthMessage.USERNAME_REQUIRED);
    }

    public static void notNullPassword(String password) throws NotNullException {
        if (password == null || password.trim().isEmpty())
            throw new NotNullException(AuthMessage.PASSWORD_REQUIRED);
    }

    public static void inValidEmail(String email) throws InValidException {
        if (!AuthUtils.isValidEmail(email.trim()))
            throw new InValidException(UserMessage.EMAIL_INVALID);
    }

    public static void inValidUsername(String username) throws InValidException {
        if (!AuthUtils.isValidUsername(username.trim()))
            throw new InValidException(UserMessage.USERNAME_INVALID);
    }

    public static void notNullRefreshToken(String token) throws NotNullException {
        if (token == null || token.trim().isEmpty())
            throw new NotNullException(AuthMessage.REFRESH_TOKEN_REQUIRED);
    }

    public static void validatorLoginDTO(LoginDTO loginDTO) {
        notNullUsername(loginDTO.getUsername());
        notNullPassword(loginDTO.getPassword());
    }

    public static void validatorRegisterDTO(RegisterDTO registerDTO) {
        notNullEmail(registerDTO.getEmail());
        inValidEmail(registerDTO.getEmail());
        notNullUsername(registerDTO.getUsername());
        inValidUsername(registerDTO.getUsername());

        registerDTO.setEmail(registerDTO.getEmail().trim());
        registerDTO.setUsername(registerDTO.getUsername().trim());
    }

    public static void validatorRefreshTokenDTO(RefreshTokenDTO refreshTokenDTO) {
        notNullRefreshToken(refreshTokenDTO.getRefreshToken());

        refreshTokenDTO.setRefreshToken(refreshTokenDTO.getRefreshToken().trim());
    }
}
