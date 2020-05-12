package cn.darkjrong.verification.common.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *  用户信息 实体类
 * @date 2020/05/11 21:23
 * @author Rong.Jia
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 5501037960736582657L;

    /**
     *  用户信息主键
     */
    private Long id;

    /**
     *  名称
     */
    private String name;

    /**
     *  账号
     */
    private String account;

    /**
     *  密码
     */
    private String password;

    /**
     *  邮箱
     */
    private String email;

    /**
     *  电话
     */
    private String telephone;


}