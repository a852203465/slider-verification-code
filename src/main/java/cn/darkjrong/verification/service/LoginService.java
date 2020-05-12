package cn.darkjrong.verification.service;

import cn.darkjrong.verification.common.pojo.dto.UserLoginDTO;
import cn.darkjrong.verification.common.pojo.vo.UserInfoVO;
import cn.darkjrong.verification.common.pojo.vo.VerificationVO;

/**
 *  登录业务层接口
 * @author Rong.Jia
 * @date 2019/08/30 19:20
 */
public interface LoginService {

    /**
     *  登录
     * @param userLoginDTO 登录信息
     * @date 2019/08/30 19:23:22
     * @author Rong.Jia
     * @return UserInfoVO 用户信息
     */
    UserInfoVO login(UserLoginDTO userLoginDTO);

    /**
     *  获取验证码
     * @param account 账号
     * @date 2019/08/30 19:23:22
     * @author Rong.Jia
     * @return  验证码
     */
    VerificationVO verificationCode(String account);

    /**
     *  检测用户
     * @param account 账号
     * @date 2019/08/30 19:23:22
     * @author Rong.Jia
     */
    void checkUser(String account);



}

