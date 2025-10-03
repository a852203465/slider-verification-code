package cn.darkjrong.verification.service;

import cn.darkjrong.verification.common.pojo.dto.ArithmeticValidateDTO;
import cn.darkjrong.verification.common.pojo.vo.VerificationVO;

/**
 * 算术服务
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
public interface ArithmeticService {

    /**
     * 生成验证码并返回验证码ID和图片
     *
     * @return {@link VerificationVO }
     */
    VerificationVO getCaptcha();

    /**
     * 验证用户输入的答案
     *
     * @param arithmeticValidateDTO 算术验证DTO
     */
    void validate(ArithmeticValidateDTO arithmeticValidateDTO);




}
