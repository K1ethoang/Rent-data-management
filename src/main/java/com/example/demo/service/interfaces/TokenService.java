package com.example.demo.service.interfaces;

public interface TokenService {
    String updateRefreshToken(String userId);

    String createRefreshToken(String userId);

    void updateAccessToken(String userId, String token);

    String getAccessToken(String username);
}
