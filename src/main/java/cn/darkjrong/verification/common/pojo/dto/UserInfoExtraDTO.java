package cn.darkjrong.verification.common.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Collection;

/**
 *  用户信息过滤查询对象
 * @author Rong.Jia
 * @date 2019/12/03 20:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfoExtraDTO extends UserInfoFilterDTO implements Serializable {

    private static final long serialVersionUID = 8572893369658025631L;

    /**
     * 账号
     */
    private String notAccount;

}
