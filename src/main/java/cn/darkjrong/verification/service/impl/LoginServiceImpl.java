package cn.darkjrong.verification.service.impl;

import cn.darkjrong.verification.common.config.AuthConfig;
import cn.darkjrong.verification.common.constants.AuthConstant;
import cn.darkjrong.verification.common.enums.ResponseEnum;
import cn.darkjrong.verification.common.exception.SliderVerificationCodeException;
import cn.darkjrong.verification.common.pojo.dto.UserLoginDTO;
import cn.darkjrong.verification.common.pojo.vo.ResponseVO;
import cn.darkjrong.verification.common.pojo.vo.UserInfoVO;
import cn.darkjrong.verification.common.pojo.vo.VerificationVO;
import cn.darkjrong.verification.common.utils.RedisUtils;
import cn.darkjrong.verification.common.utils.VerifyImageUtils;
import cn.darkjrong.verification.service.LoginService;
import cn.darkjrong.verification.service.UserInfoService;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static cn.darkjrong.verification.common.constants.AuthConstant.*;

/**
 *  登录业务层接口实现类
 * @author Rong.Jia
 * @date 2019/08/30 19:20
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private AuthConfig authConfig;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public UserInfoVO login(UserLoginDTO userLoginDTO) {

        // 账号
        String account = userLoginDTO.getAccount();

        // 查询并验证用户是否存在
        UserInfoVO userInfoVO = userInfoService.findByAccount(account);

        checkUser(account);

        // 校验验证码
        checkCaptcha(userLoginDTO.getXPos(), account);

        //  登录次数限制
        retryLimit(account, userLoginDTO.getPassword(), userInfoVO.getPassword());

        userInfoVO.setPassword(null);

        return userInfoVO;
    }

    @Override
    public VerificationVO verificationCode(String account) {

        // 读取图库目录
        File imgCatalog = this.getFile("static/img/slider/targets");
        ArrayList<File> files = CollectionUtil.newArrayList(imgCatalog.listFiles());
        if (CollectionUtil.isNotEmpty(files)) {
            // 随机选择需要切的图
            int randNum = new Random().nextInt(files.size());
            File targetFile = files.get(randNum);

            // 随机选择剪切模版
            File tempImgFile = getFile("static/img/slider/templates/" + (new Random().nextInt(6) + 1) + "-w.png");
            try {
                VerificationVO verificationVO = VerifyImageUtils.pictureTemplatesCut(tempImgFile, targetFile);

                redisUtils.set(AuthConstant.PREFIX_AUTH_VC_CODE_CACHE + account, verificationVO.getXWidth(), 1800);

                // 移除横坐标送前端
                verificationVO.setXWidth(null);

                return verificationVO;
            }catch (Exception e) {
               log.error("Captcha interception failed {}", e.getMessage());
            }
        }

        return null;
    }

    @Override
    public void checkUser(String account) {

        // 查询并验证用户是否存在
        UserInfoVO userInfoVO = userInfoService.findByAccount(account);
        checkUser(userInfoVO);

    }

    /**
     *  获取文件
     * @param dir 文件目录
     * @return 文件
     */
    private File getFile(String dir) {

        ClassPathResource classPathResource = new ClassPathResource(dir);
        return classPathResource.getFile();
    }

    /**
     *  校验验证码
     * @param xPos 验证吗
     * @param account 登录账号
     * @date 2019/07/26 13:55:33
     * @author Rong.Jia
     */
    private void checkCaptcha(Double xPos, String account) {

        // 判断账号是否为空
        if (StrUtil.isBlank(account)) {
            log.error("checkCaptcha {}", account);
            throw new SliderVerificationCodeException(ResponseEnum.ACCOUNT_IS_EMPTY);
        }

        // 判断验证码是否为空
        if (Validator.isEmpty(xPos)) {
            log.error("The verification code cannot be empty");
            throw new SliderVerificationCodeException(ResponseEnum.THE_VERIFICATION_CODE_IS_EMPTY);
        }

        // 校验验证码

        // 缓存key
        String cacheKey = AuthConstant.PREFIX_AUTH_VC_CODE_CACHE + account;

        if (!redisUtils.hasKey(cacheKey)) {
            log.error("The verification code is out of date ");
            throw new SliderVerificationCodeException(ResponseEnum.VERIFICATION_CODE_OUT_OF_DATE_PLEASE_RETRIEVE_IT_AGAIN);
        }else {

            // 正确的验证码
            Double vCode = redisUtils.get(PREFIX_AUTH_VC_CODE_CACHE + account, Double.class);

            if (xPos - vCode > 5 || xPos - vCode < - 5) {
                throw new SliderVerificationCodeException(ResponseEnum.THE_VERIFICATION_CODE_IS_INCORRECT);
            }

        }
    }

    /**
     *  校验用户
     * @param userInfoVO 用户信息
     * @date 2019/07/26 13:55:33
     * @author Rong.Jia
     */
    private void checkUser(UserInfoVO userInfoVO) {

        if (ObjectUtil.isNull(userInfoVO)) {
            log.error("checkUser {} ", "The account does not exist");
            throw new SliderVerificationCodeException(ResponseEnum.ACCOUNT_DOES_NOT_EXIST);
        }else {

            if (StrUtil.isBlank(userInfoVO.getAccount())) {
                log.error("checkUser {} ", "The account does not exist");
                throw new SliderVerificationCodeException(ResponseEnum.ACCOUNT_DOES_NOT_EXIST);
            }

        }
    }

    /**
     *  登录次数限制
     * @param account 登录账号
     * @param password 登录密码
     * @param correctPassword 账号正确密码
     * @date 2019/07/26 13:55:33
     * @author Rong.Jia
     */
    private void retryLimit(String account, String password, String correctPassword) {

        String shiroLoginCount = PREFIX_SHIRO_LOGIN_COUNT + account;
        String shiroIsLock = PREFIX_SHIRO_IS_LOCK + account;

        ValueOperations<String, Object> opsForValue = redisUtils.getRedisTemplate().opsForValue();

        // 密码进行AES解密
        String decodeAes = SecureUtil.aes(Base64.decode(authConfig.getEncryptAESKey()))
                .decryptStr(correctPassword, CharsetUtil.CHARSET_UTF_8);

        // 因为密码加密是以帐号+密码的形式进行加密的，所以解密后的对比是帐号+密码
        if (!StrUtil.equals(decodeAes, account + password)) {

            log.error("Wrong account or password");

            //访问一次，计数一次
            if (Validator.equal(USER_LOCK_UN.get(0), opsForValue.get(shiroIsLock))) {
                log.error("验证未通过,错误次数大于5次,账户已锁定  account：{}", account);
                throw new SliderVerificationCodeException(ResponseEnum.USER_NAME_OR_PASSWORD_ERRORS_GREATER_THAN_5_TIMES);

            }

            opsForValue.increment(shiroLoginCount, 1);

            //计数大于5时，设置用户被锁定一小时
            if(Convert.toInt(opsForValue.get(shiroLoginCount)) >= 5){
                opsForValue.set(shiroIsLock, USER_LOCK_UN.get(0));
                redisUtils.getRedisTemplate().expire(shiroIsLock, 1, TimeUnit.HOURS);
            }

            throw new SliderVerificationCodeException(ResponseEnum.WRONG_ACCOUNT_OR_PASSWORD);

        }else {

            //清空登录计数
            opsForValue.set(shiroLoginCount, 0);

            //设置未锁定状态
            opsForValue.set(shiroIsLock, USER_LOCK_UN.get(1));

            //  删除缓存
            redisUtils.del(PREFIX_AUTH_VC_CODE_CACHE + account);

        }
    }


}
