package com.ehang.mysql.mybatis.plus.controller;

import com.ehang.mysql.mybatis.plus.controller.dto.UserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author ehang
 * @title: UserController
 * @projectName spring-boot-010-mysql-mybatis-plus
 * @description: TODO
 * @date 2021/12/7 17:35
 */
@Slf4j
@RestController
@RequestMapping("userInfo")
public class UserController {

    @PostMapping("/map")
    public void add(@RequestBody Map<String, Object> userInfo) throws Exception {
        String userName = (String) userInfo.get("name");
        Integer age = (Integer) userInfo.get("age");

        if (StringUtils.isBlank(userName)) {
            throw new Exception("名字不能为空");
        }

        if (null == age || age < 0 || age > 120) {
            throw new Exception("名字不能小于0岁或者大于120岁");
        }

        log.info("userInfo:{}", userInfo);
    }

    @PostMapping("/obj")
    public void add(@Validated @RequestBody UserInfoDto userInfo) {
        String name = userInfo.getUserName();
        Integer age = userInfo.getAge();
        log.info("userInfo:{}", userInfo);
    }
}
