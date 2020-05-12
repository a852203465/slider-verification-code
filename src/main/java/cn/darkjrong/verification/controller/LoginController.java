package cn.darkjrong.verification.controller;

import cn.darkjrong.verification.common.enums.ResponseEnum;
import cn.darkjrong.verification.common.pojo.dto.UserLoginDTO;
import cn.darkjrong.verification.common.pojo.vo.ResponseVO;
import cn.darkjrong.verification.common.pojo.vo.UserInfoVO;
import cn.darkjrong.verification.common.pojo.vo.VerificationVO;
import cn.darkjrong.verification.common.validator.groupvlidator.UserLoginGroupValidator;
import cn.darkjrong.verification.service.LoginService;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

/**
 *  登录/登出管理 Controller层
 * @author Rong.Jia
 * @date 2019/04/17 16:01
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<UserInfoVO> login (@RequestBody UserLoginDTO userLoginDTO) {

        log.info("login {}", userLoginDTO.toString());

        UserInfoVO userInfoVO = loginService.login(userLoginDTO);

        return ResponseVO.success(userInfoVO);
    }

    @RequestMapping(value="verificationCode/{account}",method= RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseVO<VerificationVO> verificationCode(@PathVariable("account")
                            @NotBlank(message = "登录账号不能为空", groups = UserLoginGroupValidator.class) String account){

        log.info("verificationCode 获取验证码 account {}", account);

        VerificationVO verificationVO = loginService.verificationCode(account);

        return ResponseVO.success(verificationVO);
    }

    @RequestMapping(value="checkUser/{account}",method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO checkUser(@PathVariable("account")
                                    @NotBlank(message = "登录账号不能为空", groups = UserLoginGroupValidator.class) String account) {

        log.info("checkUser {}", account);

        Assert.notBlank(account, ResponseEnum.ACCOUNT_IS_EMPTY.getMessage());

        loginService.checkUser(account);

        return ResponseVO.success();

    }


}
