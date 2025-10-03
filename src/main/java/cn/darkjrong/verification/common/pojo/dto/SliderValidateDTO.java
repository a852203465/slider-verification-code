package cn.darkjrong.verification.common.pojo.dto;

import lombok.Data;

/**
 * 滑块验证DTO
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
@Data
public class SliderValidateDTO {

    private String captchaId;

    /**
     *  滑块位置
     */
    private Double xPos;


}
