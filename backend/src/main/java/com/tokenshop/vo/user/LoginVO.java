package com.tokenshop.vo.user;

import lombok.Data;

/**
 * 用户登录响应 VO
 */
@Data
public class LoginVO {

    private String token;
    private Long userId;
    private String username;
}
