package cn.darkjrong.verification.common.pojo.dto;

import lombok.Data;

/**
 * 算术验证DTO
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
@Data
public class ArithmeticValidateDTO {

    private String captchaId;

    private String userAnswer;

}
