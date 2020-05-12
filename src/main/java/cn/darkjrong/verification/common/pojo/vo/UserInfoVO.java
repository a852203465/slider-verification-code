package cn.darkjrong.verification.common.pojo.vo;

import cn.darkjrong.verification.common.pojo.dto.UserInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户信息Vo
 * @author Ring.Jia
 * @date 2019/04/17 16:19:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoVO extends UserInfoDTO implements Serializable {

    private static final long serialVersionUID = 318872322607252731L;

}
