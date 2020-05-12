package cn.darkjrong.verification.service;

import cn.darkjrong.verification.common.pojo.dto.PwdDTO;
import cn.darkjrong.verification.common.pojo.dto.UserInfoDTO;
import cn.darkjrong.verification.common.pojo.dto.UserInfoFilterDTO;
import cn.darkjrong.verification.common.pojo.vo.PageVO;
import cn.darkjrong.verification.common.pojo.vo.UserInfoVO;

/**
 * 用户信息业务层接口
 *
 * @author Rong.Jia
 * @date 2020/05/11 21:42
 */
public interface UserInfoService {

    /**
     *  新增用户信息
     * @param userInfoDTO  用户信息
     * @return 信息主键
     */
    Long saveUserInfo(UserInfoDTO userInfoDTO);

    /**
     *  修改用户信息
     * @param userInfoDTO  用户信息
     * @return 信息主键
     */
    Long updateUserInfo(UserInfoDTO userInfoDTO);

    /**
     *  根据用户账号 获取该用户信息
     * @param account 用户 账号
     * @return UserInfo 用户信息
     */
    UserInfoVO findByAccount(String account);

    /**
     *  根据用户id 修改密码
     * @param pwdDTO 密码信息
     */
    void modifyPwdById(PwdDTO pwdDTO);

    /**
     *  根据条件过滤查询用户信息
     * @param userInfoFilterDTO 过滤条件
     * @return  List<UserInfo> 用户信息集合
     */
    PageVO<UserInfoVO> findUserInfos(UserInfoFilterDTO userInfoFilterDTO);

    /**
     *  删除用户信息
     * @param userInfoId  用户主键ID
     */
    void deleteUserInfo(Long userInfoId);





}
