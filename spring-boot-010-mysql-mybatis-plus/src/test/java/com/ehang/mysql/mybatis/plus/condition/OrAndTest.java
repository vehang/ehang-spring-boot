package com.ehang.mysql.mybatis.plus.condition;

import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 多条件匹配一个即可
 */
@SpringBootTest
@Slf4j
public class OrAndTest {
    @Autowired
    UserInfoService userInfoService;

    @Test
    void or() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .le(UserInfo::getId, 10)
                .or(i -> i.eq(UserInfo::getUserName, "张三").ge(UserInfo::getId, 1005))
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id <= 10 OR (user_name = "张三" AND id >= 1005))
    }

    @Test
    void and() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .le(UserInfo::getId, 10)
                .and(i -> i.eq(UserInfo::getUserName, "一行Java 1")).list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id <= 10 AND (user_name = "一行Java 1"))
    }
}
