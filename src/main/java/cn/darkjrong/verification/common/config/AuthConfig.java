package cn.darkjrong.verification.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *  映射配置文件权限 实体类
 * @author Rong.Jia
 * @date 2019/04/17 10:02
 */
@Data
@Component
@Order(1)
@ConfigurationProperties(prefix = "auth")
public class AuthConfig {

    private String encryptAESKey;

    private String encryptJWTKey;


}
