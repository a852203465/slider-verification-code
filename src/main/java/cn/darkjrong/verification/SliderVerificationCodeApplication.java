package cn.darkjrong.verification;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("cn.darkjrong.verification.mapper")
@EnableTransactionManagement
@EnableCaching
@SpringBootApplication
public class SliderVerificationCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SliderVerificationCodeApplication.class, args);
    }

}
