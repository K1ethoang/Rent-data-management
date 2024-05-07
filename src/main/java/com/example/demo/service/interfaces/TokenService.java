package com.example.demo.service.interfaces;

import com.example.demo.entity.User;

public interface TokenService {
    String updateRefreshToken(String userId);

    String createRefreshToken(User user);

    void updateAccessToken(String userId, String token);

    String getAccessToken(String username);
}
