package com.ehang.mysql.mybatis.plus.condition;

import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 条件 不等于 ne
 */
@SpringBootTest
@Slf4j
public class NeTest {
    @Autowired
    UserInfoService userInfoService;

    @Test
    void ne() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .ne(UserInfo::getId, 1)
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id <> 1)
    }

    @Test
    void neConditionFalse() {
        // condition等于false 忽略所有的条件 也就是说eq设置不会生效
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .ne(false, UserInfo::getId, 1)
                .list();
        log.info("userInfo:{}", userInfos);

        // 等价sql：SELECT id,user_name,age,source FROM user_info
    }

    @Test
    void between() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .between(UserInfo::getId, 1, 5)
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id <> 1)
    }

    @Test
    void notBetween() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .notBetween(UserInfo::getId, 5, 100)
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id <> 1)
    }
}
