package com.example.demo.service.implement;

import com.example.demo.entity.EState;
import com.example.demo.entity.User;
import com.example.demo.exception.InValidException;
import com.example.demo.message.UserMessage;
import com.example.demo.model.DTO.user.UserDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.interfaces.UserService;
import com.example.demo.utils.validator.UserValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void createUser(UserDto userDto) {
        UserValidator.validatorUserDTO(userDto);

        checkDuplicated(userDto);

        User user = User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .state(EState.ACTIVE)
                .createDate(LocalDate.now())
                .build();

        log.info(user);

        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmailOrUsername(UserDto userDto) {
        return userRepository.findByEmailOrUsername(userDto.getEmail(),
                userDto.getUsername());

    }

    public void checkDuplicated(UserDto userDto) throws InValidException {
        if (userRepository.existsByEmail(userDto.getEmail()))
            throw new InValidException(UserMessage.EMAIL_ALREADY_EXIST);

        if (userRepository.existsByUsername(userDto.getUsername()))
            throw new InValidException(UserMessage.USERNAME_ALREADY_EXIST);
    }
}
