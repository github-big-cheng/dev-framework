package com.wisely;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wisely.ucenter.mapper")
public class UcenterWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcenterWebApplication.class, args);
    }

}
