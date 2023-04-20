package com.project.example.model.vo;

import lombok.Data;

@Data
public class UserRegisterRequest {
    /**
     * 账号
     */
    private String userAccount;
    /**
     * 密码
     */
    private String userPassword;

    private String checkUserPassword;
}
