package cn.darkjrong.verification.controller;

import cn.darkjrong.verification.common.pojo.dto.ArithmeticValidateDTO;
import cn.darkjrong.verification.common.pojo.vo.ResponseVO;
import cn.darkjrong.verification.common.pojo.vo.VerificationVO;
import cn.darkjrong.verification.service.ArithmeticService;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/arithmetic")
public class ArithmeticController {

    @Autowired
    private ArithmeticService arithmeticService;

    @GetMapping("/getCaptcha")
    public ResponseVO getCaptcha() {
        log.info("getCaptcha {}", DateUtil.now());
        VerificationVO captcha = arithmeticService.getCaptcha();
        return ResponseVO.success(captcha);
    }

    @PostMapping("/validate")
    public ResponseVO validate(@RequestBody ArithmeticValidateDTO arithmeticValidateDTO) {
        log.info("validate {}", arithmeticValidateDTO);
        arithmeticService.validate(arithmeticValidateDTO);
        return ResponseVO.success();
    }






}
