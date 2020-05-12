package cn.darkjrong.verification.common.exception;

import cn.darkjrong.verification.common.enums.ResponseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *   项目自定义异常
 * @author Rong.Jia
 * @date 2019/4/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SliderVerificationCodeException extends RuntimeException  implements Serializable {

    private static final long serialVersionUID = -1501020198729282518L;

    /**
     *  异常code 码
     */
    private Integer code;

    /**
     * 异常详细信息
     */
    private String message;

    public SliderVerificationCodeException(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public SliderVerificationCodeException(ResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public SliderVerificationCodeException(ResponseEnum responseEnum, String message) {
        super(message);
        this.code = responseEnum.getCode();
        this.message = message;
    }

    public SliderVerificationCodeException(Integer code, String message, Throwable t) {
        super(message, t);
        this.code = code;
        this.message = message;
    }

    public SliderVerificationCodeException(ResponseEnum responseEnum, Throwable t) {
        super(responseEnum.getMessage(), t);
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }


}
