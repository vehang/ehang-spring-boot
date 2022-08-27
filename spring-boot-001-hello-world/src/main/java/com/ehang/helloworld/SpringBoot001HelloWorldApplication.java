package com.ehang.helloworld;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SpringBoot001HelloWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot001HelloWorldApplication.class, args);
        log.info("启动成功！！测试变更！");
    }

}
