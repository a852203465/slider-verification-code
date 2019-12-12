package com.unionman.verification.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unionman.verification.enums.ResponseEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据格式返回统一
 * @author Rong.Jia
 * @date 2019/12/11 22:11:22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVO<T> implements Serializable {

    private static final long serialVersionUID = 3681838956784534606L;

    /**
     * 异常码
     */
    private Integer code;

    /**
     * 描述
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public ResponseVO() {}

    public ResponseVO(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public ResponseVO(Integer code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public ResponseVO(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public ResponseVO(ResponseEnum responseEnum, T data) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
        this.data = data;
    }

    public static ResponseVO success(){
        return new ResponseVO(ResponseEnum.SUCCESS);
    }

    public static <T> ResponseVO successVerify(T data){
        ResponseVO response = ResponseVO.success();
        response.setData(data);
        return response;
    }

    public static <T> ResponseVO<T> success(T data){
        return new ResponseVO<T>(ResponseEnum.SUCCESS, data);
    }

    public static <T> ResponseVO<T> error(T data){
        return new ResponseVO<T>(ResponseEnum.ERROR, data);
    }

    public static <T> ResponseVO<T> success(int code, String msg){
        return new ResponseVO<T>(code, msg);
    }

    public static ResponseVO error(int code, String msg){
        return new ResponseVO(code,msg);
    }

    public static ResponseVO error(ResponseEnum responseEnum){
        return new ResponseVO(responseEnum);
    }

    public static ResponseVO error(ResponseEnum responseEnum, Object data){
        return new ResponseVO<Object>(responseEnum, data);
    }

    public static ResponseVO errorParams(String msg){
        return new ResponseVO(ResponseEnum.PARAMETER_ERROR.getCode(), msg);
    }

}
