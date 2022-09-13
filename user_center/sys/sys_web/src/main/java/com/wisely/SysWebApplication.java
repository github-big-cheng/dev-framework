package com.wisely;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wisely.sys.mapper")
public class SysWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysWebApplication.class, args);
    }

}
