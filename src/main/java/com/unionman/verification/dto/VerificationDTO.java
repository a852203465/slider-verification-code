package com.unionman.verification.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 验证码dto 对象
 * @author Mr.J
 * @date 2019/12/11 22:24
 */
@Data
public class VerificationDTO implements Serializable {

    /**
     *  滑块位置
     */
    private Double xpos;

    /**
     *  账号
     */
    private String username;

    /**
     *  密码
     */
    private String password;

}
