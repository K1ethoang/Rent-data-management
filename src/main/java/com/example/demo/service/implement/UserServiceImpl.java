package com.example.demo.service.implement;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.enumuration.ERole;
import com.example.demo.exception.InValidException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.message.AuthMessage;
import com.example.demo.message.UserMessage;
import com.example.demo.model.DTO.user.UserDto;
import com.example.demo.model.mapper.EntityToDto;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.interfaces.UserService;
import com.example.demo.util.AuthUtils;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.validator.UserValidator;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final boolean DEFAULT_STATUS = true;

    @Override
    public void createUser(UserDto userDto) {
        UserValidator.validatorUserDTO(userDto);

        checkDuplicated(userDto);

        Optional<Role> role =
                roleRepository.findRoleByName(ERole.STAFF);

        if (role.isEmpty()) throw new NotFoundException(AuthMessage.ROLE_NOT_FOUND);

        User user = User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(AuthUtils.encodePassword(userDto.getPassword()))
                .status(DEFAULT_STATUS)
                .role(role.get())
                .build();

        userRepository.save(user);
    }

    @Override
    public UserDto getUserDetailsFromToken(String token) throws JwtException, NotFoundException {
        if (JwtUtil.isAccessTokenExpired(token)) {
            throw new JwtException(AuthMessage.TOKEN_EXPIRED);
        }

        String username = JwtUtil.extractUsername(token);
        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isEmpty())
            throw new NotFoundException(UserMessage.NOT_FOUND);

        return EntityToDto.userToDto(user.get());
    }

    @Override
    public UserDto getUserDetailsFromRefreshToken(String token) throws InValidException, JwtException {
        Optional<User> userOptional = userRepository.findUserByRefreshToken(token);

        if (userOptional.isEmpty())
            throw new InValidException(AuthMessage.REFRESH_TOKEN_INVALID);

        User user = userOptional.get();

        if (LocalDateTime.now().isAfter((user.getExpRefreshToken()))) {
            throw new JwtException(AuthMessage.TOKEN_EXPIRED);
        }

        return EntityToDto.userToDto(user);
    }

    @Override
    public UserDto getUserByUsername(String username) throws NotFoundException {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isEmpty()) throw new NotFoundException(UserMessage.NOT_FOUND);

        User user = userOptional.get();

        return EntityToDto.userToDto(user);
    }


    @Override
    public UserDto getUserDTO(String userId) {
        return EntityToDto.userToDto(getUser(userId));
    }

    @Override
    public User getUser(String userId) throws NotFoundException {
        Optional<User> userOptional = userRepository.findUserById(userId);

        if (userOptional.isEmpty()) throw new NotFoundException(UserMessage.NOT_FOUND);

        return userOptional.get();
    }

    public void checkDuplicated(UserDto userDto) throws InValidException {
        if (userRepository.existsByEmail(userDto.getEmail()))
            throw new InValidException(UserMessage.EMAIL_ALREADY_EXIST);

        if (userRepository.existsByUsername(userDto.getUsername()))
            throw new InValidException(UserMessage.USERNAME_ALREADY_EXIST);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =
                userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(UserMessage.NOT_FOUND));

        List<SimpleGrantedAuthority> grantedAuthorities =
                user.getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getAuthority())).collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), grantedAuthorities);
    }
}
