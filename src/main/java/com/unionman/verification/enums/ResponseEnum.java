package com.unionman.verification.enums;

import org.springframework.http.HttpStatus;

/**
 * @description: 数据信息状态枚举类
 * @author Rong.Jia
 * @date 2019/4/2
 */
public enum ResponseEnum {

    /**
     *  枚举类code 开头使用规则：
     *  0: 成功；
     *  -1: 失败；
     *  1：参数不正确
     *  401： 登录相关  需跳登录
     *  404：未找到
     *  405：请求方式错误
     *  415：媒体类型不支持
     */

    // 成功
    SUCCESS(0,"成功"),

    // 参数不正确
    PARAMETER_ERROR(1, "参数不正确"),

    // 失败
    ERROR(-1, "失败"),
    SYSTEM_ERROR(-1, "系统错误"),
    INT404_NOT_FOUND(-1,"找不到请求接口"),
    INT400_BAD_REQUEST(-1,"请求参数或方式错误"),
    FILE_WRITE_ERROR(-1,"文件写入失败"),
    FILE_LIMIT_EXCEEDED(-1, "文件超出限制, 请选择较小文件"),
    EXCEL_SPREADSHEET_GENERATION_FAILED(-1, "excel 表格生成失败"),
    ZIP_FILE_CREATE_FAILURE(-1, "压缩文件创建失败"),

    // 登录相关  需跳登录
    ACCESS_TOKEN_INVALID(HttpStatus.UNAUTHORIZED.value(),"access_token无效"),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED.value(),"refresh_token无效"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "无权访问(未授权)"),
    AUTHORIZATION_EXPIRES(HttpStatus.UNAUTHORIZED.value(), "授权过期, 请求重新登录"),
    NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED.value(), "未登录"),
    ANONYMOUS_SUBJECT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "无权访问:当前用户是匿名用户，请先登录"),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED.value(), "身份验证未通过"),

    // 未找到
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "请求接口不存在"),

    // 请求方式错误
    REQUEST_MODE_ERROR(HttpStatus.METHOD_NOT_ALLOWED.value(), "请求方式错误, 请检查"),

    //媒体类型不支持
    MEDIA_TYPE_NOT_SUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "媒体类型不支持"),






















    ;

    private Integer code;
    private String message;

    ResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
