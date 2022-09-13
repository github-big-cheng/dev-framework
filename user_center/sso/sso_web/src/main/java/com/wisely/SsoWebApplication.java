package com.wisely;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wisely.sso.mapper")
public class SsoWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoWebApplication.class, args);
    }

}
