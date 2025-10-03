package cn.darkjrong.verification.service;

import cn.darkjrong.verification.common.pojo.dto.SliderValidateDTO;
import cn.darkjrong.verification.common.pojo.vo.VerificationVO;

/**
 * 滑块服务
 *
 * @author Rong.Jia
 * @date 2025/10/03
 */
public interface SliderService {

    /**
     * 获取验证码
     *
     * @return {@link VerificationVO }
     */
    VerificationVO getCaptcha();

    /**
     * 验证
     *
     * @param sliderValidateDTO 滑块验证DTO
     */
    void validate(SliderValidateDTO sliderValidateDTO);




}
