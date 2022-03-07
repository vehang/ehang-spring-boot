package com.ehang.responce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 这里需要指定一下scanBasePackages 扫描的包路径
 * 否则可能导致common包下的一些公共类无法载入
 * <p>
 * 比如不加scanBasePackages，在当前项目扫描的就是com.ehang.responce目录
 * 但是common包下的一些aop相关的，就在com.ehang.common目录下，从而导致没有加载
 */
@SpringBootApplication(scanBasePackages = "com.ehang")
public class SpringBoot005ResponceExceptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot005ResponceExceptionApplication.class, args);
    }

}
