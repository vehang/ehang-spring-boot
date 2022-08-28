package com.ehang.jenkins.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LENOVO
 * @title: HelloJenkinsController
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/8/27 21:11
 */
@RestController
public class HelloJenkinsController {

    @GetMapping("/")
    public String hello() {
        return "hello jenkins!";
    }

}
