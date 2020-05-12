package cn.darkjrong.verification.common.pojo.dto;

import cn.darkjrong.verification.common.constants.RegularVerifyConstant;
import cn.darkjrong.verification.common.validator.groupvlidator.ModifyGroupValidator;
import cn.darkjrong.verification.common.validator.groupvlidator.UserInfoGroupValidator;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 *  修改密码dto 对象
 * @author Rong.Jia
 * @date 2019/04/18 12:08:22
 */
@Data
public class PwdDTO implements Serializable {

    private static final long serialVersionUID = 3482634779913187319L;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码 不能为空", groups = ModifyGroupValidator.class)
    @Pattern(regexp = RegularVerifyConstant.PWD_REG, groups = UserInfoGroupValidator.class, message = "密码格式不正确, 或者长度不足8位 / 长度超出限制")
    private String newPwd;

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能空", groups = UserInfoGroupValidator.class)
    private String oldPwd;

    /**
     * 用户账号
     */
    @NotBlank(message = "用户账号不能为空", groups = UserInfoGroupValidator.class)
    private String account;

}
