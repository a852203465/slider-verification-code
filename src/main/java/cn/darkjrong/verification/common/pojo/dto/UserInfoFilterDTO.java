package cn.darkjrong.verification.common.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *  用户信息过滤查询 dto对象
 * @author Rong.Jia
 * @date 2019/9/18 19:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInfoFilterDTO extends PageDTO implements Serializable {

    private static final long serialVersionUID = 2353430436161773708L;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String telephone;

}
