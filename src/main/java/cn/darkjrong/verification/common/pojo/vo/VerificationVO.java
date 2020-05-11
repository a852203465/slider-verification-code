package cn.darkjrong.verification.common.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 验证码vo 对象
 * @author Rong.Jia
 * @date 2019/12/11 21:49
 */
@Data
public class VerificationVO implements Serializable {

    /**
     *  滑块图
     */
    private String slidingImage;

    /**
     * 原图
     */
    private String originalImage;

    /**
     *  宽
     */
    private Integer xWidth;

    /**
     *  高
     */
    private Integer yHeight;

}
