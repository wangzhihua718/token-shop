package com.tokenshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tokenshop.entity.User;
import com.tokenshop.vo.user.LoginVO;
import com.tokenshop.vo.user.UserVO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    UserVO register(String username, String phone, String email, String password);

    /**
     * 用户登录
     */
    LoginVO login(String username, String password);

    /**
     * 获取用户信息
     */
    UserVO getUserInfo(Long userId);
}
