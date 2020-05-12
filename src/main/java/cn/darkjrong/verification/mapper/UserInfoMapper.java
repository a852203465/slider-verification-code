package cn.darkjrong.verification.mapper;

import cn.darkjrong.verification.common.pojo.dto.UserInfoExtraDTO;
import cn.darkjrong.verification.common.pojo.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  用户信息 持久层接口
 * @date 2020/05/11 21:23
 * @author Rong.Jia
 */
public interface UserInfoMapper {

    /**
     *  根据用户主键ID 删除用户信息
     * @param id 用户主键ID
     * @return 0：失败，1：成功
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新增用户信息
     * @param userInfo 用户信息
     * @return 0：失败，1：成功
     */
    int insert(UserInfo userInfo);

    /**
     *  新增用户信息
     * @param userInfo 用户信息
     * @return 0：失败，1：成功
     */
    int insertSelective(UserInfo userInfo);

    /**
     *  根据用户主键ID 查询用户信息
     * @param id 用户主键ID
     * @return  用户信息
     */
    UserInfo selectByPrimaryKey(Long id);

    /**
     *  修改用户信息
     * @param userInfo 用户信息
     * @return 0：失败，1：成功
     */
    int updateByPrimaryKeySelective(UserInfo userInfo);

    /**
     *  修改用户信息
     * @param userInfo 用户信息
     * @return 0：失败，1：成功
     */
    int updateByPrimaryKey(UserInfo userInfo);

    /**
     *  根据账号查询用户信息
     * @param account  账号
     * @return  用户信息
     */
    UserInfo findUserInfoByAccount(@Param("account") String account);

    /**
     *  根据主键修改密码
     * @param id 主键
     * @param password  密码
     * @return 0：失败，1：成功
     */
    int modifyPwdById(@Param("id") Long id, @Param("password")String password);

    /**
     *  根据条件过滤查询用户信息
     * @param userInfoExtraDTO 过滤条件
     * @return  用户信息集合
     */
    List<UserInfo> findUserInfos(UserInfoExtraDTO userInfoExtraDTO);


















}