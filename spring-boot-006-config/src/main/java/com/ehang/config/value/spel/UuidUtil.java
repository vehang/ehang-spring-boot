package com.ehang.config.value.spel;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author ehang
 * @title: UuidUtil
 * @projectName ehang-spring-boot
 * @description: TODO uuid的工具类 用户测试#{}做方法调用
 * @date 2022/3/8 17:13
 */
@Component
public class UuidUtil {

    public String getUuid() {
        return UUID.randomUUID().toString();
    }
}
