package cn.darkjrong.verification.common.pojo.bean;

import cn.darkjrong.verification.common.validator.groupvlidator.IdGroupValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *  公共属性抽层类
 * @author Rong.Jia
 * @date 2019/08/14 14:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseBean implements Serializable {

    private static final long serialVersionUID = 9023375960996498537L;

    /**
     *  主键
     */
    @NotNull(groups = IdGroupValidator.class, message = "ID 不能为空")
    private Long id;

}
