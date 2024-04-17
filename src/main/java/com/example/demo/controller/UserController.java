package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.interfaces.UserService;
import com.example.demo.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/details")
    public ResponseEntity<Object> details(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        String token = authorizationHeader.substring(JwtUtil.BEARER_PREFIX.length());

        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS,
                userService.getUserDetailsFromToken(token));
    }
}
