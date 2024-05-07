package com.example.demo.service.implement;


import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.message.UserMessage;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.interfaces.TokenService;
import com.example.demo.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {
    private static final long EXPIRED_RT = 7 * 24 * 60 * 60; // 7 days (Second)
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public String updateRefreshToken(String userId) throws NotFoundException {
        User user = userService.getUserById(userId);

        user.setRefreshToken(UUID.randomUUID().toString());

        userRepository.save(user);
        return user.getRefreshToken();
    }

    @Override
    public String createRefreshToken(User user) {
        user.setRefreshToken(UUID.randomUUID().toString());
        user.setExpRefreshToken(LocalDateTime.now().plusSeconds(EXPIRED_RT));

        userRepository.save(user);
        return user.getRefreshToken();
    }

    public void updateAccessToken(String userId, String token) {
        User user = userService.getUserById(userId);

        user.setAccessToken(token);
        userRepository.save(user);
    }

    @Override
    public String getAccessToken(String username) throws NotFoundException {
        User user =
                userRepository.findUserByUsername(username).orElseThrow(() -> new NotFoundException(UserMessage.NOT_FOUND));

        return user.getAccessToken();
    }
}
