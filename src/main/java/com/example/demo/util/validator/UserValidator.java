package com.example.demo.util.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotNullException;
import com.example.demo.message.UserMessage;
import com.example.demo.model.DTO.user.UserDto;
import com.example.demo.util.AuthUtils;

public class UserValidator {
    public static void notNullEmail(String email) throws NotNullException {
        if (email == null || email.trim().isEmpty())
            throw new NotNullException(UserMessage.EMAIL_REQUIRED);
    }

    public static void notNullUsername(String username) throws NotNullException {
        if (username == null || username.trim().isEmpty())
            throw new NotNullException(UserMessage.USERNAME_REQUIRED);
    }

    public static void inValidEmail(String email) throws InValidException {
        if (!AuthUtils.isValidEmail(email.trim()))
            throw new InValidException(UserMessage.EMAIL_INVALID);
    }

    public static void inValidUsername(String username) throws InValidException {
        if (!AuthUtils.isValidUsername(username.trim()))
            throw new InValidException(UserMessage.USERNAME_INVALID);
    }

    public static void validatorUserDTO(UserDto userDto) {
        notNullEmail(userDto.getEmail());
        inValidEmail(userDto.getEmail());
        notNullUsername(userDto.getUsername());
        inValidUsername(userDto.getUsername());

        userDto.setEmail(userDto.getEmail().trim());
        userDto.setUsername(userDto.getUsername().trim());
    }

}
