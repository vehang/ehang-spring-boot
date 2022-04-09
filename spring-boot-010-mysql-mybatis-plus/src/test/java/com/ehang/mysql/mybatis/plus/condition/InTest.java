package com.ehang.mysql.mybatis.plus.condition;

import com.ehang.mysql.mybatis.plus.generator.user.demain.UserInfo;
import com.ehang.mysql.mybatis.plus.generator.user.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 条件 包含
 */
@SpringBootTest
@Slf4j
public class InTest {
    @Autowired
    UserInfoService userInfoService;

    @Test
    void in() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .in(UserInfo::getId, 1, 2, 3)
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id IN (1, 2, 3))
    }

    @Test
    void notIn() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .notIn(UserInfo::getId, 1, 2, 3)
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id IN (1, 2, 3))
    }

    @Test
    void inSql() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .inSql(UserInfo::getId, "1, 2, 3")
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (user_name IS NULL)
    }

    @Test
    void notInSql() {
        List<UserInfo> userInfos = userInfoService.lambdaQuery()
                .notInSql(UserInfo::getId, "1, 2, 3")
                .list();
        log.info("userInfo:{}", userInfos);
        // 等价sql：SELECT id,user_name,age,source FROM user_info WHERE (id NOT IN (1, 2, 3))
    }
}
