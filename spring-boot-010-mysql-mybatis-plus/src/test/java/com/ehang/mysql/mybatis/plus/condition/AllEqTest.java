package com.ehang.mysql.mybatis.plus.condition;

import com.alibaba.fastjson.JSON;
import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AllEq
 */
@SpringBootTest
@Slf4j
public class AllEqTest {
    @Autowired
    UserInfoService userInfoService;

    Map<String, Object> params = new HashMap();

    @BeforeEach
    public void init() {
        params.put("user_name", "一行Java 1");
        params.put("id", null);
    }

    @Test
    void allEq() {
        List<UserInfo> list = userInfoService.query().allEq(params).list();
        log.info("{}", JSON.toJSONString(list));
        // 等价sql: SELECT id,user_name,age,source FROM user_info WHERE (user_name = "一行Java 1" AND id IS NULL)
    }

    @Test
    void allEqConditionFalse() {
        //-----------condition参数------------
        // 表示是否才上查询条件，如果等于false 将不会添加任何查询条件
        List<UserInfo> list = userInfoService.query().allEq(false, params, true).list();
        log.info("{}", JSON.toJSONString(list));
        // 等价sql: SELECT id,user_name,age,source FROM user_info
    }

    @Test
    void allEqNull2IsNull() {
        //-----------null2IsNull演示------------
        // null2IsNull = false;会自动踢出null值条件
        List<UserInfo> list = userInfoService.query().allEq(params, false).list();
        // 等价sql: SELECT id,user_name,age,source FROM user_info WHERE (user_name = "一行Java 1")
        log.info("{}", JSON.toJSONString(list));
    }

    @Test
    void allEqFilter() {
        //---------filter延时----------
        // filter字段，表示要忽略的字段
        // 以下是忽略key为id的条件
        List<UserInfo> list = userInfoService.query().allEq((k, v) -> !k.equals("id"), params).list();
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (user_name = "一行Java 1")
        log.info("{}", JSON.toJSONString(list));
    }
}
