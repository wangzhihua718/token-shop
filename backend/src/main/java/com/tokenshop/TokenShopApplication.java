package com.tokenshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Token Shop 启动类
 */
@SpringBootApplication
@MapperScan("com.tokenshop.mapper")
@EnableScheduling
public class TokenShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(TokenShopApplication.class, args);
        System.out.println("====================================");
        System.out.println("Token Shop 启动成功!");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("API文档: http://localhost:8080/swagger-ui.html");
        System.out.println("====================================");
    }
}
