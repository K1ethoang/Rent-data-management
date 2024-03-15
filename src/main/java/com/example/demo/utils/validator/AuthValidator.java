package com.example.demo.utils.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotNullException;
import com.example.demo.message.AuthMessage;
import com.example.demo.message.UserMessage;
import com.example.demo.model.DTO.login.LoginDTO;
import com.example.demo.model.DTO.register.RegisterDTO;
import com.example.demo.utils.AuthUtils;

public class AuthValidator {
    public static void notNullEmailOrUsername(String emailOrUsername) throws NotNullException {
        if (emailOrUsername == null || emailOrUsername.trim().isEmpty())
            throw new NotNullException(AuthMessage.EMAIL_USERNAME_REQUIRED);
    }

    public static void notNullEmail(String email) throws NotNullException {
        if (email == null || email.trim().isEmpty())
            throw new NotNullException(UserMessage.EMAIL_REQUIRED);
    }

    public static void notNullUsername(String username) throws NotNullException {
        if (username == null || username.trim().isEmpty())
            throw new NotNullException(UserMessage.USERNAME_REQUIRED);
    }

    public static void notNullPassword(String password) throws NotNullException {
        if (password == null || password.trim().isEmpty())
            throw new NotNullException(UserMessage.PASSWORD_REQUIRED);
    }

    public static void inValidEmail(String email) throws InValidException {
        if (!AuthUtils.isValidEmail(email.trim()))
            throw new InValidException(UserMessage.EMAIL_INVALID);
    }

    public static void inValidUsername(String username) throws InValidException {
        if (!AuthUtils.isValidUsername(username.trim()))
            throw new InValidException(UserMessage.USERNAME_INVALID);
    }

    public static void validatorLoginDTO(LoginDTO loginDTO) {
        notNullEmailOrUsername(loginDTO.getEmailOrUsername());
        notNullPassword(loginDTO.getPassword());

        loginDTO.setEmailOrUsername(loginDTO.getEmailOrUsername().trim());
        loginDTO.setPassword(loginDTO.getPassword().trim());
    }

    public static void validatorRegisterDTO(RegisterDTO registerDTO) {
        notNullEmail(registerDTO.getEmail());
        inValidEmail(registerDTO.getEmail());
        notNullUsername(registerDTO.getUsername());
        inValidUsername(registerDTO.getUsername());

        registerDTO.setEmail(registerDTO.getEmail().trim());
        registerDTO.setUsername(registerDTO.getUsername().trim());
    }
}
