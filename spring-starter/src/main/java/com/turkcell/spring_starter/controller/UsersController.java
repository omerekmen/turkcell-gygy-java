package com.turkcell.spring_starter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.ApiResult;
import com.turkcell.spring_starter.dto.RegisterRequest;
import com.turkcell.spring_starter.dto.LoginRequest;
import com.turkcell.spring_starter.service.UserServiceImpl;

@RequestMapping("/api/v{version:1}/users")
@RestController
public class UsersController {
    private final UserServiceImpl userService;

    public UsersController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResult<Void> register(@RequestBody RegisterRequest registerRequest)
    {
        this.userService.registerUser(registerRequest);
        return ApiResult.success(HttpStatus.CREATED.value(), "User registered successfully");
    }
    @PostMapping("/login")
    public ApiResult<String> login(@RequestBody LoginRequest loginRequest)
    {
        return ApiResult.success("Login successful", this.userService.login(loginRequest));
    }
}
