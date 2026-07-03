package com.tokenshop.vo.user;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息 VO
 */
@Data
public class UserVO {

    private Long id;
    private String username;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;
    private LocalDateTime createdAt;
}
