package cn.darkjrong.verification.common.pojo.dto;

import cn.darkjrong.verification.common.constants.RegularVerifyConstant;
import cn.darkjrong.verification.common.pojo.bean.BaseBean;
import cn.darkjrong.verification.common.validator.groupvlidator.ModifyGroupValidator;
import cn.darkjrong.verification.common.validator.groupvlidator.UserInfoGroupValidator;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 *  用户信息dto 对象
 * @date 2019/04/17 14:16:22
 * @author Rong.Jia
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoDTO extends BaseBean implements Serializable {

    private static final long serialVersionUID = -5268465359528714604L;

    /**
     * 名称（昵称或者真实姓名）
     */
    @NotBlank(message = "名称（昵称或者真实姓名）不能为空", groups = UserInfoGroupValidator.class)
    private String name;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空", groups = UserInfoGroupValidator.class)
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = ModifyGroupValidator.class)
    @Pattern(regexp = RegularVerifyConstant.PWD_REG, groups = UserInfoGroupValidator.class, message = "密码格式不正确,或者密码长度不足8位 / 长度超出限制")
    private String password;

    /**
     * 电话号码
     */
    @NotBlank(message = "电话号码 不能为空", groups = UserInfoGroupValidator.class)
    @Pattern(regexp = RegularVerifyConstant.MOBILE_REG, groups = UserInfoGroupValidator.class, message = "电话号码格式不正确")
    private String telephone;

    /**
     * 邮箱
     */
    @Pattern(regexp = RegularVerifyConstant.EMAIL_REG, groups = UserInfoGroupValidator.class, message = "邮箱格式不正确")
    private String email;


}
