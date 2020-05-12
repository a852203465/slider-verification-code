package cn.darkjrong.verification.common.pojo.dto;

import cn.darkjrong.verification.common.validator.groupvlidator.PageGroupValidator;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *  分页查询数据传输对象
 * @date 2019/02/19 18:00:22
 * @author Rong.Jia
 */
@Data
public class PageDTO implements Serializable {

    private static final long serialVersionUID = 4512708627615719846L;

    /**
     * 每页数据数量
     */
    @Min(value = 0,message = "参数必须大于0", groups = PageGroupValidator.class)
    private Integer pageSize;

    /**
     * 页码(开始页:1, 默认1，-1：所有)
     */
    @NotNull(message = "参数必传，传-1代表不分页", groups = PageGroupValidator.class)
    @Min(value = -1,message = "参数必须大于-1", groups = PageGroupValidator.class)
    private Integer currentPage;

    /**
     * 排序类型（asc,desc）
     */
    private String orderType;

    /**
     * 排序字段
     */
    private String orderField;


}
