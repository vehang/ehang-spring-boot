package com.ehang.mysql.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.ehang.mysql.mybatis")
public class SpringBoot009MysqlMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot009MysqlMybatisApplication.class, args);
    }

}
