package com.example.demo.controller;

import com.example.demo.message.GlobalMessage;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/details")
    public ResponseEntity<Object> details(@RequestHeader("Authorization") String authorizationHeader) {
        return ApiResponse.responseBuilder(HttpStatus.OK, GlobalMessage.SUCCESS,
                userService.getUserDetailsFromToken(authorizationHeader));
    }
}
