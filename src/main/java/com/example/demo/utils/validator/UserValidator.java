package com.example.demo.utils.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotNullException;
import com.example.demo.message.UserMessage;
import com.example.demo.model.DTO.user.UserDto;

public class UserValidator {
    public static void notNullEmail(String email) throws NotNullException {
        if (email == null || email.trim().isEmpty())
            throw new NotNullException(UserMessage.NOT_NULL_EMAIL);
    }

    public static void notNullUsername(String username) throws NotNullException {
        if (username == null || username.trim().isEmpty())
            throw new NotNullException(UserMessage.NOT_NULL_USERNAME);
    }

    public static void inValidEmail(String email) throws InValidException {

    }

    public static void inValidUsername(String username) throws InValidException {

    }

    public static void validatorUserDTO(UserDto userDto) {
        notNullEmail(userDto.getEmail());
        notNullUsername(userDto.getUsername());

        userDto.setEmail(userDto.getEmail().trim());
        userDto.setUsername(userDto.getUsername().trim());
    }

}
