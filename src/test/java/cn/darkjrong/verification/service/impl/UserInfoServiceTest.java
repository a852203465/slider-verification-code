package cn.darkjrong.verification.service.impl;

import cn.darkjrong.verification.SliderVerificationCodeApplicationTests;
import cn.darkjrong.verification.common.pojo.dto.UserInfoDTO;
import cn.darkjrong.verification.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户信息测试类
 * @author Rong.Jia
 * @date 2020/05/11 23:17
 */
class UserInfoServiceTest extends SliderVerificationCodeApplicationTests {

    @Autowired
    private UserInfoService userInfoService;

    @Test
    void saveUserInfoTest() {

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setAccount("admin");
        userInfoDTO.setName("贾荣");
        userInfoDTO.setTelephone("15019202295");
        userInfoDTO.setPassword("JIArong207718");
        userInfoDTO.setEmail("852203465@qq.com");

        Long aLong = userInfoService.saveUserInfo(userInfoDTO);

        System.out.println(aLong);
    }

    @Test
    void updateUserInfoTest() {
    }

    @Test
    void findByAccountTest() {
    }

    @Test
    void modifyPwdByIdTest() {
    }

    @Test
    void findUserInfosTest() {
    }

    @Test
    void deleteUserInfoTest() {
    }
}