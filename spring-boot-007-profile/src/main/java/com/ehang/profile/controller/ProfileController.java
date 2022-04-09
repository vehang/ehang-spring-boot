package com.ehang.profile.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LENOVO
 * @title: ProfileController
 * @projectName ehang-spring-boot
 * @description: TODO
 * @date 2022/3/31 22:32
 */
@RestController
public class ProfileController {
    @Value("${va}")
    private Integer va;

    @GetMapping
    public Integer getVa() {
        return va;
    }
}
