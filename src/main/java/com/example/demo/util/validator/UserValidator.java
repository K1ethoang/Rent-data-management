package com.example.demo.util.validator;

import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotNullException;
import com.example.demo.message.UserMessage;
import com.example.demo.model.DTO.ChangePasswordDto;
import com.example.demo.model.DTO.user.UserDto;
import com.example.demo.model.DTO.user.UserUpdateDto;
import com.example.demo.util.AuthUtils;

public class UserValidator {
    private static int MIN_LENGTH_PASSWORD = 8;

    public static void notNullEmail(String email) throws NotNullException {
        if (email == null || email.trim().isEmpty())
            throw new NotNullException(UserMessage.EMAIL_REQUIRED);
    }

    public static void notNullFullName(String fullName) throws NotNullException {
        if (fullName == null || fullName.trim().isEmpty())
            throw new NotNullException(UserMessage.FULLNAME_REQUIRED);
    }

    public static void notNullCurrentPassword(String currentPassword) throws NotNullException {
        if (currentPassword == null || currentPassword.isEmpty())
            throw new NotNullException(UserMessage.CURRENT_PASSWORD_REQUIRED);
    }

    public static void notNullNewPassword(String newPassword) throws NotNullException {
        if (newPassword == null || newPassword.isEmpty())
            throw new NotNullException(UserMessage.NEW_PASSWORD_REQUIRED);
    }

    public static void inValidEmail(String email) throws InValidException {
        if (!AuthUtils.isValidEmail(email.trim()))
            throw new InValidException(UserMessage.EMAIL_INVALID);
    }

    public static void inValidPassword(String password) throws InValidException {
        if (password.length() < MIN_LENGTH_PASSWORD)
            throw new InValidException(UserMessage.PASSWORD_INVALID);
    }

    public static void validatorUserDTO(UserDto userDto) {
        notNullEmail(userDto.getEmail());
        notNullFullName(userDto.getFullName());
        inValidEmail(userDto.getEmail());
        // todo invalid fullname have number

        userDto.setEmail(userDto.getEmail().trim().toLowerCase());
        userDto.setFullName(userDto.getFullName().trim());
    }

    public static void validatorUserUpdateDTO(UserUpdateDto userUpdateDto) {
        if (userUpdateDto.getEmail() != null) {
            notNullEmail(userUpdateDto.getEmail());
            inValidEmail(userUpdateDto.getEmail());
            userUpdateDto.setEmail(userUpdateDto.getEmail().trim().toLowerCase());
        }

        if (userUpdateDto.getFullName() != null) {
            notNullFullName(userUpdateDto.getFullName());
            userUpdateDto.setFullName(userUpdateDto.getFullName().trim());
        }
    }

    public static void validatorChangePasswordDTO(ChangePasswordDto changePasswordDto) {
        notNullCurrentPassword(changePasswordDto.getCurrentPassword());
        notNullNewPassword(changePasswordDto.getNewPassword());
        inValidPassword(changePasswordDto.getNewPassword());
    }
}
