package com.example.demo.service.implement;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.enumuration.ERole;
import com.example.demo.exception.DuplicatedException;
import com.example.demo.exception.InValidException;
import com.example.demo.exception.NoContentException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.message.AuthMessage;
import com.example.demo.message.UserMessage;
import com.example.demo.model.DTO.paging.APIPageableDTO;
import com.example.demo.model.DTO.user.UserDto;
import com.example.demo.model.DTO.user.UserUpdateDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final boolean DEFAULT_STATUS = true;
    private final String DEFAULT_PASSWORD = "123@456";
    private final boolean BLOCK_STATUS = false;
    private final boolean UN_BLOCK_STATUS = true;


    @Override
    public Map<String, Object> getAll(Pageable pageable) throws NoContentException {
        Map<String, Object> result = new HashMap<>();

        Page<User> pageEntity =
                userRepository.findAll(pageable);

        Page<UserDto> pageDto =
                pageEntity.map(EntityToDto::userToDto);

        APIPageableDTO apiPageableDTO = new APIPageableDTO(pageDto);

        result.put("page", apiPageableDTO);
        result.put("users", pageDto.getContent());

        return result;
    }

    @Override
    public void createUser(UserDto userDto) {
        UserValidator.validatorUserDTO(userDto);

        checkDuplicated(userDto);

        // Default role
        Optional<Role> role =
                roleRepository.findRoleByName(ERole.STAFF);

        if (role.isEmpty()) throw new NotFoundException(AuthMessage.ROLE_NOT_FOUND);

        User user = User.builder()
                .email(userDto.getEmail())
                .username(userDto.getUsername())
                .password(AuthUtils.encodePassword(DEFAULT_PASSWORD))
                .active(DEFAULT_STATUS)
                .fullName(userDto.getFullName())
                .role(role.get())
                .build();

        userRepository.save(user);
    }

    @Override
    public void updateUser(String id, UserUpdateDto userUpdateDto, String token) throws JwtException, InValidException {
        if (JwtUtil.isAccessTokenExpired(token)) {
            throw new JwtException(AuthMessage.TOKEN_EXPIRED);
        }
        // Kiểm tra có phai user đó đang cập nhật profile mình không
        String username = JwtUtil.extractUsername(token);
        User userFromDb = getUser(id);

        if (JwtUtil.extractRole(token).equals(ERole.MANAGER.toString()) || username.equals(userFromDb.getUsername())) {
            UserValidator.validatorUserUpdateDTO(userUpdateDto);

            UserDto tempUser = EntityToDto.userToDto(userFromDb);

            // Update cho role staff
            if (userUpdateDto.getEmail() != null) {
                tempUser.setEmail(userUpdateDto.getEmail());
            }
            if (userUpdateDto.getUsername() != null) {
                tempUser.setUsername(userUpdateDto.getUsername());
            }
            if (userUpdateDto.getFullName() != null) {
                tempUser.setFullName(userUpdateDto.getFullName());
            }

            // Update chỉ có manager
            if (JwtUtil.extractRole(token).equals(ERole.MANAGER.toString())) {
                if (userUpdateDto.getActive() != null) {
                    tempUser.setActive(userUpdateDto.getActive());
                }
                if (userUpdateDto.getRole() != null) {
                    tempUser.setRole(userUpdateDto.getRole());
                }
            }

            checkDuplicated(tempUser);

            Optional<Role> roleNew =
                    roleRepository.findRoleByName(ERole.STAFF);

            if (roleNew.isEmpty()) throw new NotFoundException(AuthMessage.ROLE_NOT_FOUND);

            userFromDb.setEmail(tempUser.getEmail());
            userFromDb.setUsername(tempUser.getUsername());
            userFromDb.setFullName(tempUser.getFullName());
            userFromDb.setActive(tempUser.getActive());
            userFromDb.setRole(roleNew.get());

            userRepository.save(userFromDb);
        } else
            throw new InValidException(HttpStatus.FORBIDDEN.toString());
    }

    @Override
    public void blockUser(String id) {
        User userFromDb = getUser(id);

        userFromDb.setActive(BLOCK_STATUS);

        userRepository.save(userFromDb);
    }

    @Override
    public void unBlockUser(String id) {
        User userFromDb = getUser(id);

        userFromDb.setActive(UN_BLOCK_STATUS);

        userRepository.save(userFromDb);
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

    // Không được trùng username, email
    public void checkDuplicated(UserDto userToCheck) throws DuplicatedException {
        List<User> userList = userRepository.findAll();

        for (User user : userList) {
            // kiểm tra xem có phải customer đang check có trong danh sách không
            if (userToCheck.getId() != null && userToCheck.getId().equals(user.getId()))
                continue;

            UserDto userFromList = EntityToDto.userToDto(user);

            if (userFromList.getEmail().equals(userToCheck.getUsername())) {
                throw new DuplicatedException(UserMessage.EMAIL_ALREADY_EXIST);
            }
            if (userFromList.getUsername().equals(userToCheck.getUsername())) {
                throw new DuplicatedException(UserMessage.USERNAME_ALREADY_EXIST);
            }
        }
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
