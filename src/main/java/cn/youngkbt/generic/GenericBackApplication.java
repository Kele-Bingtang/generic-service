package cn.youngkbt.generic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Kele-Bingtang
 * @date 2022/11/23 21:17
 * @note Spring Boot 启动类
 */
@SpringBootApplication
@MapperScan("cn.youngkbt.generic.base.mapper")
public class GenericBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(GenericBackApplication.class, args);
    }
}
