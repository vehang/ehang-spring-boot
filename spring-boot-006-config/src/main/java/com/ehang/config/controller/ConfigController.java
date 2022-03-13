package com.ehang.config.controller;

import com.ehang.config.cp.UserConfigByCP;
import com.ehang.config.value.simple.UserConfigByValue;
import com.ehang.config.value.spel.UserConfigByValueSpEL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LENOVO
 * @title: ConfigController
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/7 23:10
 */
@RestController
@RequestMapping("config")
public class ConfigController {

    @Autowired
    UserConfigByCP userConfig;

    @Autowired
    UserConfigByValue userConfigByValue;

    @Autowired
    UserConfigByValueSpEL userConfigByValueSpEL;

    /**
     * 获取通过 @ConfigurationProperties 注入的属性
     * 地址：http://127.0.0.1:8086/user/cp
     *
     * @return
     */
    @GetMapping("user/cp")
    private UserConfigByCP userByCP() {
        return userConfig;
    }

    /**
     * 获取通过 @Value("${}") 注入的属性
     * 地址：http://127.0.0.1:8086/user/value
     *
     * @return
     */
    @GetMapping("user/value")
    private UserConfigByValue userByValue() {
        return userConfigByValue;
    }

    /**
     * 获取通过SpEL对配置文件进行更复杂的操作
     * 地址：http://127.0.0.1:8086/user/value/spel
     *
     * @return
     */
    @GetMapping("user/value/spel")
    private UserConfigByValueSpEL userByValueSpEL() {
        return userConfigByValueSpEL;
    }
}
