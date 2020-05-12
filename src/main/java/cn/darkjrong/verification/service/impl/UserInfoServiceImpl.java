package cn.darkjrong.verification.service.impl;

import cn.darkjrong.verification.common.config.AuthConfig;
import cn.darkjrong.verification.common.constants.AuthConstant;
import cn.darkjrong.verification.common.constants.RegularVerifyConstant;
import cn.darkjrong.verification.common.enums.ResponseEnum;
import cn.darkjrong.verification.common.exception.SliderVerificationCodeException;
import cn.darkjrong.verification.common.pojo.dto.PwdDTO;
import cn.darkjrong.verification.common.pojo.dto.UserInfoDTO;
import cn.darkjrong.verification.common.pojo.dto.UserInfoExtraDTO;
import cn.darkjrong.verification.common.pojo.dto.UserInfoFilterDTO;
import cn.darkjrong.verification.common.pojo.entity.UserInfo;
import cn.darkjrong.verification.common.pojo.vo.PageVO;
import cn.darkjrong.verification.common.pojo.vo.UserInfoVO;
import cn.darkjrong.verification.common.utils.PageableUtils;
import cn.darkjrong.verification.mapper.UserInfoMapper;
import cn.darkjrong.verification.service.UserInfoService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 用户信息业务层接口实现类
 * @author Rong.Jia
 * @date 2020/05/11 22:02
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private AuthConfig authConfig;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Long saveUserInfo(UserInfoDTO userInfoDTO) {

        UserInfo userInfo = userInfoMapper.findUserInfoByAccount(userInfoDTO.getAccount());
        if (ObjectUtil.isNotNull(userInfo)) {
            throw new SliderVerificationCodeException(ResponseEnum.USER_INFORMATION_ALREADY_EXISTS);
        }

        // 校验密码
        if (!ReUtil.isMatch(RegularVerifyConstant.PWD_REG, userInfoDTO.getPassword())) {
            log.error("saveUserInfo() Password format is incorrect, or password length is less than 8 bits/length exceeds the limit");
            throw new SliderVerificationCodeException(ResponseEnum.INCORRECT_PASSWORD_FORMAT_OR_INCORRECT_PASSWORD_LENGTH);
        }

        userInfo = new UserInfo();
        BeanUtil.copyProperties(userInfoDTO, userInfo);

        // 加密后的密码
        String aeSencode = SecureUtil.aes(Base64.decode(authConfig.getEncryptAESKey()))
                .encryptHex(userInfoDTO.getAccount() + userInfoDTO.getPassword());
        userInfo.setPassword(aeSencode);

        userInfoMapper.insert(userInfo);

        return userInfo.getId();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Long updateUserInfo(UserInfoDTO userInfoDTO) {

        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userInfoDTO.getId());

        Assert.notNull(userInfo, ResponseEnum.USER_INFO_INFO_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED.getMessage());

        // 不允许修改账号名
        if (!StrUtil.equals(userInfoDTO.getAccount(), userInfo.getAccount())) {
            log.error("updateUserInfo() Account cannot be changed");
            throw new SliderVerificationCodeException(ResponseEnum.ACCOUNT_CANNOT_BE_CHANGED);
        }

        userInfo.setName(userInfoDTO.getName());
        userInfo.setTelephone(userInfoDTO.getTelephone());
        userInfo.setEmail(userInfoDTO.getEmail());

        userInfoMapper.updateByPrimaryKey(userInfo);

        return userInfo.getId();
    }

    @Override
    public UserInfoVO findByAccount(String account) {

        Assert.notBlank(account, ResponseEnum.ACCOUNT_IS_EMPTY.getMessage());

        UserInfo userInfo = userInfoMapper.findUserInfoByAccount(account);

        if (ObjectUtil.isNotNull(userInfo)) {
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtil.copyProperties(userInfo, userInfoVO);
            return userInfoVO;
        }

        return null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void modifyPwdById(PwdDTO pwdDTO) {

        // 判断密码长度
        if (!ReUtil.isMatch(RegularVerifyConstant.PWD_REG, pwdDTO.getNewPwd())) {
            log.error("modifyPwd() Password format is incorrect, or password length is less than 8 bits/length exceeds the limit");
            throw new SliderVerificationCodeException(ResponseEnum.INCORRECT_PASSWORD_FORMAT_OR_INCORRECT_PASSWORD_LENGTH);
        }

        UserInfo userInfo = userInfoMapper.findUserInfoByAccount(pwdDTO.getAccount());

        Assert.notNull(userInfo, ResponseEnum.USER_INFO_INFO_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED.getMessage());

        // 密码进行AES解密
        String decodeAES = SecureUtil.aes(Base64.decode(authConfig.getEncryptAESKey())).decryptStr(userInfo.getPassword());

        // 校验原密码
        if (!StrUtil.equals(decodeAES, pwdDTO.getAccount() + pwdDTO.getOldPwd())) {
            log.error("Please check if the original password is entered correctly");
            throw new SliderVerificationCodeException(ResponseEnum.PASSWORD_CHECK_FAILED);
        }

        // 校验新密码与旧密码是否相同
        if (StrUtil.isNotBlank(pwdDTO.getNewPwd())) {
            if (StrUtil.equals(decodeAES, pwdDTO.getAccount() + pwdDTO.getNewPwd())) {
                log.error("The old password is the same as the new one. Please re-enter the new one");
                throw new SliderVerificationCodeException(ResponseEnum.THE_OLD_PASSWORD_IS_THE_SAME_AS_THE_NEW_ONE);
            }

            // 校验新密码
            if (!ReUtil.isMatch(RegularVerifyConstant.PWD_REG, pwdDTO.getNewPwd())) {
                log.error("Password format is incorrect or password length is less than 8 bits/length exceeding the limit");
                throw new SliderVerificationCodeException(ResponseEnum.INCORRECT_PASSWORD_FORMAT_OR_INCORRECT_PASSWORD_LENGTH);
            }
        }

        // 加密
        String aeSencode = SecureUtil.aes(Base64.decode(authConfig.getEncryptAESKey()))
                .encryptHex(pwdDTO.getAccount() + pwdDTO.getNewPwd());

        userInfoMapper.modifyPwdById(userInfo.getId(), aeSencode);
    }

    @Override
    public PageVO<UserInfoVO> findUserInfos(UserInfoFilterDTO userInfoFilterDTO) {

        PageVO<UserInfoVO> pageVO = new PageVO<>();

        UserInfoExtraDTO userInfoExtraDTO = new UserInfoExtraDTO();
        BeanUtil.copyProperties(userInfoFilterDTO, userInfoExtraDTO);
        userInfoExtraDTO.setNotAccount(Optional.ofNullable(userInfoExtraDTO.getNotAccount()).orElse(AuthConstant.ADMINISTRATOR));

        List<UserInfo> userInfos;
        if(userInfoFilterDTO.getCurrentPage() < 0){
            userInfos = userInfoMapper.findUserInfos(userInfoExtraDTO);
            Optional.ofNullable(userInfos).ifPresent(u -> pageVO.setTotal(u.size()));
        }else {
            PageableUtils.basicPage(userInfoFilterDTO.getCurrentPage(), userInfoFilterDTO.getPageSize(),
                    userInfoFilterDTO.getOrderType(), userInfoFilterDTO.getOrderField());
            PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfoMapper.findUserInfos(userInfoExtraDTO));
            userInfos = pageInfo.getList();
            pageVO.setTotalPages(pageInfo.getPages());
            pageVO.setHasNext(pageInfo.isHasNextPage());
            pageVO.setHasPrevious(pageInfo.isHasPreviousPage());
            pageVO.setIsFirst(pageInfo.isIsFirstPage());
            pageVO.setIsLast(pageInfo.isIsLastPage());
            pageVO.setTotal((int)pageInfo.getTotal());
            pageVO.setCurrentPage(pageInfo.getPageNum());
            pageVO.setPageSize(pageInfo.getSize());
        }

        if (CollectionUtil.isNotEmpty(userInfos)) {
            List<UserInfoVO> userInfoVoList = new ArrayList<>();
            assert userInfos != null;
            for (UserInfo userInfo :userInfos) {
                UserInfoVO userInfoVO = new UserInfoVO();
                BeanUtil.copyProperties(userInfo, userInfoVO);
                userInfoVoList.add(userInfoVO);
            }
            pageVO.setRecords(userInfoVoList);
        }

        return pageVO;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteUserInfo(Long userInfoId) {

        Assert.notNull(userInfoId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userInfoId);
        Optional.ofNullable(userInfo).orElseThrow(()
                -> new SliderVerificationCodeException(ResponseEnum.USER_INFO_INFO_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED));

        // 判断是否管理员
        if(AuthConstant.ADMINISTRATOR.equals(userInfo.getAccount())){
            log.error("deleteUserInfoById()  System administrator cannot delete");
            throw new SliderVerificationCodeException(ResponseEnum.SYSTEM_ADMINISTRATOR_CANNOT_DELETE);
        }

        userInfoMapper.deleteByPrimaryKey(userInfoId);

    }
}
