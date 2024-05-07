package com.example.demo.util.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotNullException;
import com.example.demo.message.UserMessage;
import com.example.demo.model.DTO.user.UserDto;
import com.example.demo.model.DTO.user.UserUpdateDto;
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

    public static void notNullFullName(String fullName) throws NotNullException {
        if (fullName == null || fullName.trim().isEmpty())
            throw new NotNullException(UserMessage.FULLNAME_REQUIRED);
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
        notNullUsername(userDto.getUsername());
        notNullFullName(userDto.getFullName());
        inValidEmail(userDto.getEmail());
        inValidUsername(userDto.getUsername());
        // todo invalid fullname have number

        userDto.setEmail(userDto.getEmail().trim().toLowerCase());
        userDto.setUsername(userDto.getUsername().trim().toLowerCase());
        userDto.setFullName(userDto.getFullName().trim());
    }

    public static void validatorUserUpdateDTO(UserUpdateDto userUpdateDto) {
        if (userUpdateDto.getEmail() != null) {
            notNullEmail(userUpdateDto.getEmail());
            inValidEmail(userUpdateDto.getEmail());
            userUpdateDto.setEmail(userUpdateDto.getEmail().trim().toLowerCase());
        }

        if (userUpdateDto.getUsername() != null) {
            notNullUsername(userUpdateDto.getUsername());
            inValidUsername(userUpdateDto.getUsername());
            userUpdateDto.setUsername(userUpdateDto.getUsername().trim().toLowerCase());
        }

        if (userUpdateDto.getFullName() != null) {
            notNullFullName(userUpdateDto.getFullName());
            userUpdateDto.setFullName(userUpdateDto.getFullName().trim());
        }
    }
}
