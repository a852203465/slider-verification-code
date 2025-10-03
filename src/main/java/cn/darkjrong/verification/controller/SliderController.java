package cn.darkjrong.verification.controller;

import cn.darkjrong.verification.common.pojo.dto.SliderValidateDTO;
import cn.darkjrong.verification.common.pojo.vo.ResponseVO;
import cn.darkjrong.verification.common.pojo.vo.VerificationVO;
import cn.darkjrong.verification.service.SliderService;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/slider")
public class SliderController {

    @Autowired
    private SliderService sliderService;

    @PostMapping("/validate")
    public ResponseVO validateCaptcha(@RequestBody SliderValidateDTO sliderValidateDTO) {
        log.info("validateCaptcha {}", sliderValidateDTO);
        sliderService.validate(sliderValidateDTO);
        return ResponseVO.success();
    }

    @GetMapping(value = "getCaptcha", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseVO<VerificationVO> getCaptcha() {
        log.info("getCaptcha {}", DateUtil.now());
        VerificationVO verificationVO = sliderService.getCaptcha();
        return ResponseVO.success(verificationVO);
    }




}
