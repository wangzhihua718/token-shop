package com.tokenshop.controller;

import com.tokenshop.common.Result;
import com.tokenshop.entity.User;
import com.tokenshop.service.UserService;


import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户控制器
 */

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Resource
    private UserService userService;

    public static class RegisterRequest {
        private String username;
        private String phone;
        private String email;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginResponse {
        private String token;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }

    @PostMapping
    
    public Result<User> register(@RequestBody RegisterRequest request) {
        User user = userService.register(request.getUsername(), request.getPhone(),
                request.getEmail(), request.getPassword());
        user.setPassword(null);
        return Result.success(user);
    }

    @PostMapping("/login")
    
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return Result.success(response);
    }

    @GetMapping("/profile")
    
    public Result<User> getUserInfo(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        Long userId = com.tokenshop.util.JwtUtil.getUserIdFromToken(token);
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }
}
