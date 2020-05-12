package cn.darkjrong.verification.common.pojo.dto;

import cn.darkjrong.verification.common.validator.groupvlidator.UserLoginGroupValidator;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *  用户登录 dto
 * @author Rong.Jia
 * @date 2019/04/17 16:27
 */
@Data
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = 611509515495545755L;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空", groups = UserLoginGroupValidator.class)
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = UserLoginGroupValidator.class)
    private String password;

    /**
     *  滑块位置
     */
    @NotNull(message = "滑块位置不能为空", groups = UserLoginGroupValidator.class)
    private Double xPos;

}
