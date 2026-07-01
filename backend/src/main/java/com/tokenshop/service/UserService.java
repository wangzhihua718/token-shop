package com.tokenshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tokenshop.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    User register(String username, String phone, String email, String password);

    /**
     * 用户登录
     */
    String login(String username, String password);

    /**
     * 获取用户信息
     */
    User getUserInfo(Long userId);
}
