package com.ehang.config.controller;

import com.ehang.config.beans.UserConfig;
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
    UserConfig userConfig;

    @GetMapping("user")
    private UserConfig user(){
        return userConfig;
    }
}
