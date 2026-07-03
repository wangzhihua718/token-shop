package com.tokenshop.controller;

import com.tokenshop.common.Result;
import com.tokenshop.dto.user.LoginDTO;
import com.tokenshop.dto.user.RegisterDTO;
import com.tokenshop.service.UserService;
import com.tokenshop.vo.user.LoginVO;
import com.tokenshop.vo.user.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户控制器
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping
    public Result<UserVO> register(@Valid @RequestBody RegisterDTO request) {
        UserVO user = userService.register(request.getUsername(), request.getPhone(),
                request.getEmail(), request.getPassword());
        return Result.success(user);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO request) {
        LoginVO response = userService.login(request.getUsername(), request.getPassword());
        return Result.success(response);
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/profile")
    public Result<UserVO> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserVO user = userService.getUserInfo(userId);
        return Result.success(user);
    }
}
